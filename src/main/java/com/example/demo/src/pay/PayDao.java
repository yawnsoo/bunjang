package com.example.demo.src.pay;
import com.example.demo.src.bungaeTalk.model.*;
import com.example.demo.src.pay.model.PostPayHistoryReq;
import com.example.demo.src.pay.model.PostPayHistoryRes;
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
public class PayDao {

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


    //결제내역 생성
    @Transactional
    public PostPayHistoryRes createPaymentDetail (PostPayHistoryReq postPayHistoryReq)
    {
        String createPaymentDetailQuery = "insert into payment_detail(buyer_id,post_id,used_point,total_price,method) VALUES(?,?,?,?,?)";
        Object[] createPaymentDetailParams = new Object[]{postPayHistoryReq.getBuyer_id(),postPayHistoryReq.getPost_id(),postPayHistoryReq.getUsed_point(),postPayHistoryReq.getTotal_price(),postPayHistoryReq.getMethod()};

        this.jdbcTemplate.update(createPaymentDetailQuery,createPaymentDetailParams);

        //결제되었으므로 해당 post의 status를 0으로 바꿔줌.
        String postStatusChange = "update post set status =0 where post_id =?";
        this.jdbcTemplate.update(postStatusChange,postPayHistoryReq.getPost_id());

        //used_point가 0이 아니라면 포인트 변동 내역을 추가해줌.
        String createPointHistory = "insert into point_history(user_id,amount) VALUES(?,?)";
        Object[] createPointHistoryParams = new Object[]{postPayHistoryReq.getBuyer_id(),postPayHistoryReq.getUsed_point()};
        this.jdbcTemplate.update(createPointHistory,createPointHistoryParams);


        String temp = "select last_insert_id()";
        int payment_detail_id = this.jdbcTemplate.queryForObject(temp,int.class);

        PostPayHistoryRes postPayHistoryRes = new PostPayHistoryRes(payment_detail_id, postPayHistoryReq.getBuyer_id(), postPayHistoryReq.getPost_id(), postPayHistoryReq.getUsed_point(), postPayHistoryReq.getTotal_price(), postPayHistoryReq.getMethod());
        return postPayHistoryRes;

    }

    public int checkPostStatus (int post_id)
    {
        String checkPostStatusQuery = "select status from post where post_id = ?";
        int result = this.jdbcTemplate.queryForObject(checkPostStatusQuery,int.class,post_id);
        return result;
    }


}
