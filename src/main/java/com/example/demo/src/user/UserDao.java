package com.example.demo.src.user;


import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetUserRes> getUsers(){
        String getUsersQuery = "select * from user";
        return this.jdbcTemplate.query(getUsersQuery,
                (rs,rowNum) -> new GetUserRes(
                        rs.getInt("user_id"),
                        rs.getString("login_id"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("profile_Image_url"),
                        rs.getString("name"),
                        rs.getString("phone_number"),
                        rs.getString("agree_on_mail"),
                        rs.getString("agree_on_sms"),
                        rs.getInt("point"),
                        rs.getString("create_at"),
                        rs.getString("updated_at"),
                        rs.getBoolean("status"),
                        rs.getString("nickname")
                ));
    }

    public List<GetUserRes> getUsersByEmail(String email){
        String getUsersByEmailQuery = "select * from user where email =?";
        String getUsersByEmailParams = email;
        return this.jdbcTemplate.query(getUsersByEmailQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("user_id"),
                        rs.getString("login_id"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("profile_Image_url"),
                        rs.getString("name"),
                        rs.getString("phone_number"),
                        rs.getString("agree_on_mail"),
                        rs.getString("agree_on_sms"),
                        rs.getInt("point"),
                        rs.getString("create_at"),
                        rs.getString("updated_at"),
                        rs.getBoolean("status"),
                        rs.getString("nickname")
                ),

                getUsersByEmailParams);
    }

    public GetUserRes getUser(int user_id){
        String getUserQuery = "select * from user where user_id = ?";
        int getUserParams = user_id;
        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("user_id"),
                        rs.getString("login_id"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("profile_Image_url"),
                        rs.getString("name"),
                        rs.getString("phone_number"),
                        rs.getString("agree_on_mail"),
                        rs.getString("agree_on_sms"),
                        rs.getInt("point"),
                        rs.getString("create_at"),
                        rs.getString("updated_at"),
                        rs.getBoolean("status"),
                        rs.getString("nickname")
                ),
                getUserParams);
    }

    public List<GetAddressRes> getAddress(int user_id){
        String getAddressQuery = "select * from user_address where user_id = ?";
        int getUserParams = user_id;
        return this.jdbcTemplate.query(getAddressQuery,
                (rs, rowNum) -> new GetAddressRes(
                        rs.getInt("user_id"),
                        rs.getInt("user_address_id"),
                        rs.getString("address_name"),
                        rs.getString("full_address"),
                        rs.getBoolean("is_main_address")
                ),
                getUserParams);
    }
    

    public int createUser(PostUserReq postUserReq){
        String createUserQuery = "insert into user (login_id,password,email,name,phone_number,nickname,agree_on_mail,agree_on_sms)  VALUES (?,?,?,?,?,?,?,?)";
        Object[] createUserParams = new Object[]{postUserReq.getLogin_id(),postUserReq.getPassword(),postUserReq.getEmail(),postUserReq.getName(),postUserReq.getPhone_number(),postUserReq.getNickname(),postUserReq.getAgree_on_mail(),postUserReq.getAgree_on_sms()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }
    public int createAddress(PostAddressReq postAddressReq){
        String createAddressQuery = "insert into user_address (user_id,address_name,full_address,is_main_address)  VALUES (?,?,?,?)";
        Object[] createAddressParams = new Object[]{postAddressReq.getUser_id(),postAddressReq.getAddress_name(),postAddressReq.getFull_address(),postAddressReq.is_main_address()};
        this.jdbcTemplate.update(createAddressQuery, createAddressParams);

        String lastInserIdQuery = "select last_insert_id()"; // 생성 성공후 auto increment값을 가져옴
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }

    public int checkEmail(String email){
        String checkEmailQuery = "select exists(select email from user where email = ?)";
        String checkEmailParams = email;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);
    }
    public int checkLogin_id(String login_id){
        String checkLogin_idQuery = "select exists(select login_id from user where login_id = ?)";
        String checkLogin_idParams = login_id;
        return this.jdbcTemplate.queryForObject(checkLogin_idQuery,
                int.class,
                checkLogin_idParams);
    }
    public int checkNickname(String nickname){
        String checkNicknameQuery = "select exists(select nickname from user where nickname = ?)";
        String checkNicknameParams = nickname;
        return this.jdbcTemplate.queryForObject(checkNicknameQuery,
                int.class,
                checkNicknameParams);
    }
    public int changeMainaddress(){
        String changeMainAddressQuery = "update user_address set is_main_address = false";
        return this.jdbcTemplate.update(changeMainAddressQuery);
    }

    public int modifyUserNickName(PatchUserReq patchUserReq){
        String modifyUserNickNameQuery = "update user set nickname = ? where user_id = ? ";
        Object[] modifyUserNameParams = new Object[]{patchUserReq.getNickname(), patchUserReq.getUser_id()};

        return this.jdbcTemplate.update(modifyUserNickNameQuery,modifyUserNameParams);
    }
    public int modifyUserProfile_Image_url(PatchUserReq patchUserReq){
        String modifyUserImageQuery = "update user set profile_Image_url = ? where user_id = ? ";
        Object[] modifyUserNameParams = new Object[]{patchUserReq.getProfile_Image_url(), patchUserReq.getUser_id()};

        return this.jdbcTemplate.update(modifyUserImageQuery,modifyUserNameParams);
    }
    public int modifyUserPassword(PatchUserReq patchUserReq){
        String modifyUserPasswordQuery = "update user set password = ? where user_id = ? ";
        Object[] modifyUserNameParams = new Object[]{patchUserReq.getPassword(), patchUserReq.getUser_id()};

        return this.jdbcTemplate.update(modifyUserPasswordQuery,modifyUserNameParams);
    }
    public int modifyUserPhone_number(PatchUserReq patchUserReq){
        String modifyUserPhoneQuery = "update user set phone_number = ? where user_id = ? ";
        Object[] modifyUserNameParams = new Object[]{patchUserReq.getPhone_number(), patchUserReq.getUser_id()};

        return this.jdbcTemplate.update(modifyUserPhoneQuery,modifyUserNameParams);
    }
    public int modifyUserAgree_on_mail(PatchUserReq patchUserReq){
        String modifyUserAgree_Mail_Query = "update user set agree_on_mail = ? where user_id = ? ";
        Object[] modifyUserNameParams = new Object[]{patchUserReq.getAgree_on_mail(), patchUserReq.getUser_id()};

        return this.jdbcTemplate.update(modifyUserAgree_Mail_Query,modifyUserNameParams);
    }
    public int modifyUserAgree_on_sms(PatchUserReq patchUserReq){
        String modifyUserAgree_Sms_Query = "update user set agree_on_sms = ? where user_id = ? ";
        Object[] modifyUserNameParams = new Object[]{patchUserReq.getAgree_on_sms(), patchUserReq.getUser_id()};

        return this.jdbcTemplate.update(modifyUserAgree_Sms_Query,modifyUserNameParams);
    }


    public User getPwd(PostLoginReq postLoginReq){
        String getPwdQuery = "select * from user where login_id = ?";
        String getPwdParams = postLoginReq.getLogin_id();

        return this.jdbcTemplate.queryForObject(getPwdQuery,
                (rs,rowNum)-> new User(
                        rs.getInt("user_id"),
                        rs.getString("login_id"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("profile_Image_url"),
                        rs.getString("name"),
                        rs.getString("phone_number"),
                        rs.getString("agree_on_mail"),
                        rs.getString("agree_on_sms"),
                        rs.getInt("point"),
                        rs.getString("create_at"),
                        rs.getString("updated_at"),
                        rs.getBoolean("status"),
                        rs.getString("nickname")

                ),
                getPwdParams
                );

    }

    public int deleteUser(int user_id){
        String deleteUser_Query = "delete from user where user_id = ? ";
        int deleteUser_param = user_id;

        return this.jdbcTemplate.update(deleteUser_Query,deleteUser_param);
    }
    public int deleteAddress(int user_address_id){
        String deleteAddress_Query = "delete from user_address where user_address_id = ? ";
        int deleteAddress_param = user_address_id;

        return this.jdbcTemplate.update(deleteAddress_Query,deleteAddress_param);
    }




}
