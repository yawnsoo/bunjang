package com.example.demo.src.point;

import com.example.demo.config.BaseException;
import com.example.demo.src.bungaeTalk.TalkDao;
import com.example.demo.src.bungaeTalk.TalkProvider;
import com.example.demo.src.bungaeTalk.model.PostTalkMessageReq;
import com.example.demo.src.bungaeTalk.model.PostTalkMessageRes;
import com.example.demo.src.bungaeTalk.model.PostTalkRoomReq;
import com.example.demo.src.bungaeTalk.model.PostTalkRoomRes;
import com.example.demo.src.oauth.model.PostKakaoLoginReq;
import com.example.demo.src.oauth.model.PostKakaoLoginRes;
import com.example.demo.src.point.model.PostPointHistoryReq;
import com.example.demo.src.point.model.PostPointHistoryRes;
import com.example.demo.src.user.UserDao;
import com.example.demo.src.user.UserProvider;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class PointService {

    private final PointDao pointDao;
    private final PointProvider pointProvider;
    private final JwtService jwtService;


    @Autowired
    public PointService(PointDao pointDao, PointProvider pointProvider, JwtService jwtService) {
        this.pointDao = pointDao;
        this.pointProvider = pointProvider;
        this.jwtService = jwtService;

    }



    //포인트 내역 생성 api
    @Transactional
    public PostPointHistoryRes createPointHistory(PostPointHistoryReq pointHistoryReq) throws BaseException
    {
        try{
            PostPointHistoryRes postPointHistoryRes = pointDao.createPointHistory(pointHistoryReq);
            return postPointHistoryRes;

        }catch (Exception exception)
        {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
