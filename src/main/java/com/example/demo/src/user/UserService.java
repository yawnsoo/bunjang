package com.example.demo.src.user;



import com.example.demo.config.BaseException;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    //일반회원가입
    @Transactional
    public PostJoinRes createUser(PostJoinReq postJoinReq) throws BaseException
    {
        try{
            int user_id = userDao.creatUser(postJoinReq);
            //jwt발급
            String jwt = jwtService.createJwt(user_id);
            return new PostJoinRes(jwt,user_id);

        }catch (Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }


    //회원탈퇴
    @Transactional
    public PatchWithdrawRes withdraw(int user_id, PatchWithdrawReq patchWithdrawReq) throws BaseException
    {
        try{
            return userDao.withdraw(user_id,patchWithdrawReq);

        }
        catch (Exception exception)
        {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }



    //상점(유저)정보 수정
    @Transactional
    public String reviseUserInfo(int user_id,PatchReviseReq patchReviseReq) throws BaseException
    {
        try{
            int result = userDao.reviseUserInfo(user_id,patchReviseReq);
            String result_return ;
            if(result ==1)
            {
                result_return = "정보수정 성공";
                return result_return;
            }
            else{
                result_return = "정보수정 실패";
                return result_return;
            }

        }
        catch (Exception exception)
        {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }




}
