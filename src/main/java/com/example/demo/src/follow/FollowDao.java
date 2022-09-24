package com.example.demo.src.follow;

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
public class FollowDao {

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
    public int createFollow(PostFollowReq postFollowReq)
    {
        //팔로우 했던 기록이 있다면(다시 언팔을 했던간에) 새로 만들필요없이 status값만 바꿔주면됨
        String checkExistFollowId = "select exists(select follow_id from follow where follwer_id =? and followee_id = ?)";
        Object[] checkExistFollowIdParams = new Object[]{postFollowReq.getFollwer_id(),postFollowReq.getFollowee_id()};

        int result = this.jdbcTemplate.queryForObject(checkExistFollowId,int.class,checkExistFollowIdParams);
        if(result ==1) // 이미 존재하는거니까 status만 바꿔줌 ( 0이였으면 1로 , 1이였으면 0으로)
        {
            String check_status = "select status from follow where follwer_id =? and followee_id = ?";
            int status = this.jdbcTemplate.queryForObject(check_status,int.class,checkExistFollowIdParams);

            if( status == 0 )
            {
                String chage_status = "update follow set status = 1 where follwer_id =? and followee_id = ?";
                this.jdbcTemplate.update(chage_status,checkExistFollowIdParams);
            }
            if( status == 1)
            {
                String chage_status = "update follow set status = 0 where follwer_id =? and followee_id = ?";
                this.jdbcTemplate.update(chage_status,checkExistFollowIdParams);
            }

        }
        else{
            //팔로우했던 기록이없다면 새로만들어 주면됨.
            String createFollowQuery = "insert into follow(follwer_id,followee_id) VALUES (?,?)";
            this.jdbcTemplate.update(createFollowQuery,checkExistFollowIdParams);
        }



        String check_status = "select status from follow where follwer_id =? and followee_id = ?";
        int status = this.jdbcTemplate.queryForObject(check_status,int.class,checkExistFollowIdParams);

        return status; // status를 리턴.
    }

    //팔로워 숫자체크
    public int followerNumber (int user_id)
    {
        String checkNumber = "select count(*) from follow where followee_id = ? and status = 1";
        int number = this.jdbcTemplate.queryForObject(checkNumber,int.class,user_id);

        return number;
    }
    //팔로잉 숫자체크
    public int followingNumber (int user_id)
    {
        String checkNumber = "select count(*) from follow where follwer_id = ? and status = 1";
        int number = this.jdbcTemplate.queryForObject(checkNumber,int.class,user_id);

        return number;
    }


}
