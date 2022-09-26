package com.example.demo.src.user;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@Service
public class UserProvider {

    private final UserDao userDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserProvider(UserDao userDao, JwtService jwtService) {
        this.userDao = userDao;
        this.jwtService = jwtService;
    }

    //---중복체크
    //상점이름 중복체크
    public int checkMarketName(String market_name) throws BaseException{
        try{
            return userDao.checkMarketName(market_name);
        }catch (Exception exception)
        {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);

        }
    }
    public int checkMarketName2(String market_name,int user_id) throws BaseException{
        try{
            return userDao.checkMarketName2(market_name,user_id);
        }catch (Exception exception)
        {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);

        }
    }


    //일반 로그인
    public PostLoginRes login(PostLoginReq postLoginReq) throws BaseException
    {
        try{
            int user_id = userDao.login(postLoginReq);
            if(user_id ==0) // 없는 유저라는것이므로 종료
            {
                throw new BaseException(NOT_EXIST_USER);
            }
            if(user_id ==-1) // 탈퇴한 회원임을 알린다.
            {
                throw new BaseException(WITHDRAW_USER);
            }
            else{
                String jwt =jwtService.createJwt(user_id);

                PostLoginRes postLoginRes = new PostLoginRes();
                postLoginRes.setUser_id(user_id);
                postLoginRes.setJwt(jwt);

                return postLoginRes;
            }


        }
        catch (Exception exception)
        {
            exception.printStackTrace();
            throw exception; // not_exist_user을 날리기위해.
        }


    }
    public GetUserInfoRes getUser(int user_id) throws BaseException
    {
        try{
            return userDao.getUser(user_id);

        }
        catch (Exception exception)
        {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkUser(int user_id) throws BaseException
    {
        try{
            return userDao.checkUserStatus(user_id);

        }catch (Exception exception)
        {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }






}
