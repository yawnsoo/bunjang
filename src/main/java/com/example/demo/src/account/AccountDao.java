package com.example.demo.src.account;
import com.example.demo.src.account.model.GetAccountRes;
import com.example.demo.src.account.model.PostAccountReq;
import com.example.demo.src.bungaeTalk.model.*;
import com.example.demo.src.follow.model.PostFollowReq;
import com.example.demo.src.point.model.PostPointHistoryReq;
import com.example.demo.src.point.model.PostPointHistoryRes;
import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class AccountDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int checkUserStatus(int user_id) // 존재하는 유저인지 , 탈퇴한 유저인지 체크하는 함수.
    //없는유저라면 0, 탈퇴한유저라면 -1 , 존재하는 유저라면 해당 유저아이디를 리턴해준다.
    {
        String existUserQuery = "select user_id from user where user_id = ?";
        String result = "";
        try{
            result = this.jdbcTemplate.queryForObject(existUserQuery,String.class,user_id);
        }
        catch (EmptyResultDataAccessException e)
        {
            result ="empty";
        }
        if(result.equals("empty")) //해당 정보를 가지고있는 유저가없어 catch문에 걸려서 empty가 저장됬다면.
        {
            return 0; //0을 주어 그런유저가 없다는것을 알린다.
        }
        else{ // 아니면 해당 유저의 index를 줌, 그리고 그 유저의 status를 체크한다.0이면 1로바꿔주고 로그인 시켜줌.

            String checkStatusQuery = "select status from user where user_id =?";
            int checkStatusParam = Integer.parseInt(result);

            if(this.jdbcTemplate.queryForObject(checkStatusQuery,int.class,checkStatusParam) == 0)
            { //탈퇴한 회원이라 status가 0이라면
                return -1; // -1을 리턴하여 탈퇴한 회원임을 알린다. ( 재회원가입을 유도하고 , 상점명만 바꿔주고 , status바꿔주면될듯)
            }

            return Integer.parseInt(result);
        }
    }


    @Transactional
    public int createAccount(PostAccountReq postAccountReq)
    {
        String accountQuery = "insert into account(name,is_default_account,account_number,bank_id) VALUES(?,?,?,?)";
        Object[] accountParams = new Object[]{postAccountReq.getName(),postAccountReq.getIs_default_account(),postAccountReq.getAccount_number(),postAccountReq.getBank_id()};
        this.jdbcTemplate.update(accountQuery,accountParams);
        String temp = "select last_insert_id()";
        int account_id = this.jdbcTemplate.queryForObject(temp,int.class);

        //계좌 등록후 account_id와 user_id를 이용하여 user_account 정보생성.
        String createQuery = "insert into user_account(user_id,account_id) VALUES(?,?)";
        this.jdbcTemplate.update(createQuery,postAccountReq.getUser_id(),account_id);

        int user_account_id = this.jdbcTemplate.queryForObject(temp,int.class);

        return user_account_id;

    }

    public void checkDefault(int user_id) // default가 이미있는지 체크하고 validation
    {
        String checkQu = "select a.account_id  from account a\n" +
                "        join user_account ua on a.account_id = ua.account_id\n" +
                "                 where user_id = ? and is_default_account =1";

        int id = this.jdbcTemplate.queryForObject(checkQu,int.class,user_id);

        String upQu = "update account set is_default_account = 0 where account_id = ?";
        this.jdbcTemplate.update(upQu,id);

    }

    public List<GetAccountRes> getAccounts(int user_id)
    {
        String qq = "select ua.user_account_id,b.name as 은행이름 ,a.account_number as 계좌번호 , a.name as 예금주이름 from account as a\n" +
                "join user_account ua on a.account_id = ua.account_id\n" +
                "         join bank b on b.bank_id = a.bank_id\n" +
                "where user_id = ?";
        return this.jdbcTemplate.query(qq,(rs, rowNum) ->new GetAccountRes(
                rs.getInt("user_account_id"),
                rs.getString("은행이름"),
                rs.getString("계좌번호"),
                rs.getString("예금주이름")
        ),user_id );

    }

    @Transactional
    public void deleteAccount(int user_account_id,int user_id)
    {
        //user_account_id를 이용하여 account_id를 가져오고 , 두개의 테이블에서 각각 삭제
        String bb = "select account_id from user_account where user_account_id = ?";
        int account_id = this.jdbcTemplate.queryForObject(bb,int.class,user_account_id);

        //user_account에서 삭제하고
        String del = "delete from user_account where user_account_id = ?";
        String del2 = "delete from account where account_id = ?";

        this.jdbcTemplate.update(del,user_account_id);
        this.jdbcTemplate.update(del2,account_id);

        //계좌가 1개이상 더 있으면 가장 첫번째꺼를 기본계좌로 만들어준다.
        String check ="select exists(select * from user_account where user_id = ?)";
        int r = this.jdbcTemplate.queryForObject(check,int.class,user_id);
        if(r == 1)
        {
            String cc ="select account_id from user_account where user_id = ? limit 1";
            int a_account_id = this.jdbcTemplate.queryForObject(cc,int.class,user_id);
            String dd= "update account set is_default_account = 1 where account_id = ?";
            this.jdbcTemplate.update(dd,a_account_id);
        }
    }


}
