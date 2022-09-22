package com.example.demo.src.follow;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.follow.model.PostFollowReq;
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
public class FollowProvider {

    private final FollowDao followDao;
    private final JwtService jwtService;

    @Autowired
    public FollowProvider(FollowDao followDao , JwtService jwtService)
    {
        this.followDao = followDao;
        this.jwtService = jwtService;

    }

    public int checkUser(int user_id) throws BaseException
    {
        try{
            return followDao.checkUserStatus(user_id);

        }catch (Exception exception)
        {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }





}
