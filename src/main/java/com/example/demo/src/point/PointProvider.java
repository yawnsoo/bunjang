package com.example.demo.src.point;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.oauth.AuthDao;
import com.example.demo.src.oauth.model.PostKakaoLoginRes;
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
public class PointProvider {

    private final PointDao pointDao;
    private final JwtService jwtService;

    @Autowired
    public PointProvider(PointDao pointDao , JwtService jwtService)
    {
        this.pointDao = pointDao;
        this.jwtService = jwtService;

    }

    public int checkUser(int user_id) throws BaseException
    {
        try{
            return pointDao.checkUserStatus(user_id);

        }catch (Exception exception)
        {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkUserPoint(int user_id) throws BaseException
    {
        try{
            return pointDao.checkUserPoint(user_id);

        }catch (Exception exception)
        {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkPointIDExist(int user_id) throws BaseException
    {
        try{
            return pointDao.checkPointIDExist(user_id);

        }catch (Exception exception)
        {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }

    }



}
