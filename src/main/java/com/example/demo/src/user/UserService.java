package com.example.demo.src.user;



import com.example.demo.config.BaseException;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class UserService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserDao userDao;
    private final UserProvider userProvider;
    private final JwtService jwtService;


    @Autowired
    public UserService(UserDao userDao, UserProvider userProvider, JwtService jwtService) {
        this.userDao = userDao;
        this.userProvider = userProvider;
        this.jwtService = jwtService;

    }

    //POST
    public PostUserRes createUser(PostUserReq postUserReq) throws BaseException {
        //중복
        //이메일중복
        if(userProvider.checkEmail(postUserReq.getEmail()) ==1){
            throw new BaseException(POST_USERS_EXISTS_EMAIL);
        }
        //Login_id 중복
        if(userProvider.checkLogin_id(postUserReq.getLogin_id()) ==1){
            throw new BaseException(POST_USERS_EXISTS_LOGIN_ID);
        }
        //닉네임 중복
        if(userProvider.checkNickname(postUserReq.getNickname()) ==1){
            throw new BaseException(POST_USERS_EXISTS_NICKNAME);
        }

        String pwd;
        try{
            //암호화
            pwd = new SHA256().encrypt(postUserReq.getPassword());
            postUserReq.setPassword(pwd);

        } catch (Exception ignored) {
            ignored.printStackTrace();
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }
        try{
            int user_id = userDao.createUser(postUserReq);
            //jwt 발급.
            String jwt = jwtService.createJwt(user_id);
            return new PostUserRes(jwt,user_id);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);

        }
    }
    public PostAddressRes createAddress(PostAddressReq postAddressReq) throws BaseException {

        try{
            if(postAddressReq.is_main_address()) // 메인주소로 지정하기 위해 true값이 들어왔으면 다른 true는 false로 바꿔줌
            {
                userProvider.changeMainaddress();
            }
            
            int user_address_id = userDao.createAddress(postAddressReq);

           
            return new PostAddressRes(user_address_id,postAddressReq.getUser_id());
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);

        }




    }

    public void modifyUserNickName(PatchUserReq patchUserReq) throws BaseException {

        if(userProvider.checkNickname(patchUserReq.getNickname()) ==1){ // 닉네임 중복체크
            throw new BaseException(POST_USERS_EXISTS_NICKNAME);
        }
        try{
            int result = userDao.modifyUserNickName(patchUserReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USER_NICKNAME);
            }
        } catch(Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public void modifyUserProfile_Image_url(PatchUserReq patchUserReq) throws BaseException {
        try{
            int result = userDao.modifyUserProfile_Image_url(patchUserReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USER_IMAGE);
            }
        } catch(Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public void modifyUserPassWord(PatchUserReq patchUserReq) throws BaseException {
        String pwd;
        try{
            //암호화
            pwd = new SHA256().encrypt(patchUserReq.getPassword());
            patchUserReq.setPassword(pwd);

        } catch (Exception ignored) {
            ignored.printStackTrace();
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }
         //암호화 하고 난뒤 값을 보냄.
        try{
            int result = userDao.modifyUserPassword(patchUserReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USER_PASSWORD);
            }
        } catch(Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }

    }
    public void modifyUserPhone_number(PatchUserReq patchUserReq) throws BaseException {
        try{
            int result = userDao.modifyUserPhone_number(patchUserReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USER_PHONE_NUMBER);
            }
        } catch(Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public void modifyUserAgree_on_mail(PatchUserReq patchUserReq) throws BaseException {
        try{
            int result = userDao.modifyUserAgree_on_mail(patchUserReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USER_AGREE_ON_MAIL);
            }
        } catch(Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public void modifyUserAgree_on_sms(PatchUserReq patchUserReq) throws BaseException {
        try{
            int result = userDao.modifyUserAgree_on_sms(patchUserReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USER_AGREE_ON_SMS);
            }
        } catch(Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deleteUser(int user_id) throws BaseException {
        try{
            int result = userDao.deleteUser(user_id);
            if(result == 0){
                throw new BaseException(DELETE_USER_ERROR);
            }
        } catch(Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deleteAddress(int user_address_id) throws BaseException {
        try{
            int result = userDao.deleteAddress(user_address_id);
            if(result == 0){
                throw new BaseException(DELETE_ADDRESS_ERROR);
            }
        } catch(Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }



}
