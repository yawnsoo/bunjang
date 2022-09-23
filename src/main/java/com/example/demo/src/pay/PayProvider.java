package com.example.demo.src.pay;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.oauth.AuthDao;
import com.example.demo.src.oauth.model.PostKakaoLoginRes;
import com.example.demo.src.point.PointDao;
import com.example.demo.src.user.model.PostLoginReq;
import com.example.demo.src.user.model.PostLoginRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.example.demo.utils.JwtService;
import org.springframework.transaction.annotation.Transactional;
import static com.example.demo.config.BaseResponseStatus.*;
@Service
public class PayProvider {
    private final PayDao payDao;
    private final JwtService jwtService;

    @Autowired
    public PayProvider(PayDao payDao , JwtService jwtService)
    {
        this.payDao = payDao;
        this.jwtService = jwtService;

    }

    public int checkUser(int user_id) throws BaseException
    {
        try{
            return payDao.checkUserStatus(user_id);

        }catch (Exception exception)
        {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }


    public int checkPostStatus (int post_id) throws BaseException
    {
        try{
            return payDao.checkPostStatus(post_id);

        }catch (Exception exception)
        {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }


}
