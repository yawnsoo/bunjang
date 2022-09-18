package com.example.demo.src.user;


import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }



    //일반회원가입
    @Transactional
    public int creatUser(PostJoinReq postJoinReq)
    {
        String checkWithdrawUserQuery = "select user_id from user where name = ? and registration_number = ? and phone_number =?";
        Object[] checkParams = new Object[]{postJoinReq.getName(),postJoinReq.getRegistration_number(),postJoinReq.getPhone_number()};

        try{ //회원탈퇴했던 유저라면
            int user_id = this.jdbcTemplate.queryForObject(checkWithdrawUserQuery,int.class,checkParams);
            //status를 1로 바꿔주고 , 새로입력한 상점이름으로 바꿔준다.
            String changeStatusQuery = "update user set status =1, market_name =? where user_id = ?";
            Object[] changeStatusParams = new Object[]{postJoinReq.getMarket_name(),user_id};
            this.jdbcTemplate.update(changeStatusQuery,changeStatusParams);
            //탈퇴 테이블의 해당 유저의 status는 0으로 바꿔준다.
            String changeQuery = "update withdraw_user_list set status =0 where user_id =?";
            this.jdbcTemplate.update(changeQuery,user_id);
            return user_id;

        }catch (EmptyResultDataAccessException e)
        { //아니라면
            String createUserQuery = "insert into user(name,registration_number,phone_number,mobile_carrier_id,market_name) VALUES (?,?,?,?,?)";
            Object[] needParams = new Object[]{postJoinReq.getName(),postJoinReq.getRegistration_number(),postJoinReq.getPhone_number(),postJoinReq.getMobile_carrier_id(),postJoinReq.getMarket_name()};
            this.jdbcTemplate.update(createUserQuery,needParams);
            String lastInsertUserIdQuery = "select last_insert_id()"; // 마지막 user_id를 가져올 쿼리
            return this.jdbcTemplate.queryForObject(lastInsertUserIdQuery,int.class);
        }


    }


    //중복체크
    //상점이름 중복체크
    public int checkMarketName(String market_name)
    {
        String checkMarketNameQuery ="select exists(select market_name from user where market_name = ?)";
        String checkMarketNameParam =market_name;
        return this.jdbcTemplate.queryForObject(checkMarketNameQuery,int.class,checkMarketNameParam);

    }

    //로그인
    public int login(PostLoginReq postLoginReq)
    {
        String loginQuery = "select user_id from user where name =? and registration_number =? and phone_number =?";
        Object[] loginParams = new Object[]{postLoginReq.getName(),postLoginReq.getRegistration_number(),postLoginReq.getPhone_number()};
        String result;
        try{
            result = this.jdbcTemplate.queryForObject(loginQuery,String.class,loginParams);
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

    //회원탈퇴
    @Transactional
    public PatchWithdrawRes withdraw(int user_id , PatchWithdrawReq patchWithdrawReq)
    {
        //회원탈퇴 테이블에 값추가하고 , 유저테이블의 status를 바꾸는 2개의 작업 필요
        String withdrawUserAddQuery = "insert into withdraw_user_list(user_id,reason) VALUES(?,?)";
        Object[] withdrawUserAddParams = new Object[]{user_id,patchWithdrawReq.getReason()};

        this.jdbcTemplate.update(withdrawUserAddQuery,withdrawUserAddParams);

        String changeStatusQuery= "update user set status =0 where user_id = ?";
        int changeStatusParam = user_id;

        this.jdbcTemplate.update(changeStatusQuery,changeStatusParam);

        String returnWithdrawUserQuery = "select user_id,status from user where user_id = ?";
        int returnWithdrawUserParam = user_id;

        PatchWithdrawRes patchResult = jdbcTemplate.queryForObject(returnWithdrawUserQuery , (rs, rowNum) -> new PatchWithdrawRes(
                rs.getInt("user_id"),
                rs.getInt("status")
                ),
                returnWithdrawUserParam
        );

        patchResult.setReason(patchWithdrawReq.getReason());
        return patchResult;

    }

    //상점(유저)정보 수정
    @Transactional
    public int reviseUserInfo(int user_id,PatchReviseReq patchReviseReq)
    {
        String reviseQuery = "update user set image_path =?,market_name =?,content =? where user_id =?";
        Object[] reviseParams = new Object[]{patchReviseReq.getImage_path(),patchReviseReq.getMarket_name(),patchReviseReq.getContent(),user_id};

        return this.jdbcTemplate.update(reviseQuery,reviseParams); //성공하면 1 아니면 다른숫자.
    }




}
