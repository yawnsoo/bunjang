package com.example.demo.src.oauth;

import com.example.demo.src.oauth.model.userid_registration;
import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AuthDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int checkKakaoId (String kakao_id,String name , String birthday)
    {
        String checkKakaoQuery = "select exists(select kakao_id from user where kakao_id = ?)";
        String checkKakaoparam = kakao_id;

        int result =this.jdbcTemplate.queryForObject(checkKakaoQuery,int.class,checkKakaoparam);

        if(result == 1 )
        {//이미 카카오 연동된 아이디가 있다면 1을 리턴
            return 1;
        }
        else
        {//0이라면 , 일반회원은 가입되어있지만, 카카오연동이 안된 회원이라면 카카오고유아이디를 추가해준다.
            // 일반회원가입까지 되어있지않아서 연동실패했으면 0을 리턴해줌(일반회원가입조차안된회원)
//            String checkQuery = "select user_id,registration_number from user where name =?";
//            String checkParam = name;

//            List<userid_registration> result_list = this.jdbcTemplate.query(checkQuery, (rs, rowNum) -> new userid_registration(
//                    rs.getInt("user_id"),
//                    rs.getString("registration_number")
//            ),checkParam);
//            for(userid_registration temp : result_list) //해당이름을 가진사람의 유저아이디와 주민등록번호 리스트를 가져온다.
//            {
//                int user_id = temp.getUser_id();
//                String reg_num = temp.getRegistration_number();
//                //카카오서버에서 받아온 이름과 user데이터의 이름이 같은 사람에 대해 생년월일까지 비교한다.
//                //이름, 생년월일까지 같으면 일반회원가입으로 가입한 사람이므로 카카오 고유번호를 추가해준다.
//                System.out.println("reg_num = " + reg_num);
//                System.out.println("user_id = " + user_id);
//                if(reg_num.substring(2,6).equals(birthday)) //같다면 일반회원가입은 했지만 카카오 연동은 하지않은사람.
//                { // 카카오연동( = 카카오고유번호를 데이터에 추가) 해준다.
//                    String addKakaoQuery ="update user set kakao_id = ? where user_id =?";
//                    Object[] addKakaoParams = new Object[]{kakao_id,user_id};
//                    this.jdbcTemplate.update(addKakaoQuery,addKakaoParams);
//                    return 1; //연동시켰으니 1을 리턴.
//                }
//
//            }
            //이름만이용해서 원래회원인지 체크하는부분입니다.
            String check = "select exists(select user_id from user where name = ?)";
            int check_result = this.jdbcTemplate.queryForObject(check,int.class,name);
            if(check_result == 1)
            {
                String checkQuery = "select user_id from user where name =?";
                String checkParam = name;
                int result_id = this.jdbcTemplate.queryForObject(checkQuery,int.class,checkParam);
                String addKakaoQuery ="update user set kakao_id = ? where user_id =?";
                Object[] addKakaoParams = new Object[]{kakao_id,result_id};
                this.jdbcTemplate.update(addKakaoQuery,addKakaoParams);
                return 1;
            }

            return 0;
        }

    }

    @Transactional
    public int kakaoLogin (String kakao_id) // 카카오 고유번호를 이용하여 해당 유저번호를 가져옴 , 카카오아이디는 String형식
    {
        String kakaoLoginQuery = "select user_id from user where kakao_id = ?";
        String kakaoLoginParam = kakao_id;
        String result;

        try{
            result = this.jdbcTemplate.queryForObject(kakaoLoginQuery,String.class,kakaoLoginParam);
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



}
