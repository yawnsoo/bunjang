package com.example.demo.src.bungaeTalk;


import com.example.demo.config.BaseException;
import com.example.demo.src.bungaeTalk.model.PostTalkMessageReq;
import com.example.demo.src.bungaeTalk.model.PostTalkMessageRes;
import com.example.demo.src.bungaeTalk.model.PostTalkRoomReq;
import com.example.demo.src.bungaeTalk.model.PostTalkRoomRes;
import com.example.demo.src.oauth.model.PostKakaoLoginReq;
import com.example.demo.src.oauth.model.PostKakaoLoginRes;
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
public class TalkService {
    private final TalkDao talkDao;
    private final TalkProvider talkProvider;
    private final JwtService jwtService;


    @Autowired
    public TalkService(TalkDao talkDao, TalkProvider talkProvider, JwtService jwtService) {
        this.talkDao = talkDao;
        this.talkProvider = talkProvider;
        this.jwtService = jwtService;

    }

    @Transactional
    public PostTalkRoomRes createRoom(PostTalkRoomReq postTalkRoomReq) throws BaseException
    {
        try{
            PostTalkRoomRes postTalkRoomRes = talkDao.createRoom(postTalkRoomReq);
            return postTalkRoomRes;

        }catch (Exception exception)
        {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public PostTalkMessageRes createMessage(PostTalkMessageReq postTalkMessageReq) throws BaseException
    {
        try{
            PostTalkMessageRes postTalkMessageRes = talkDao.createMessage(postTalkMessageReq);
            return postTalkMessageRes;

        }catch (Exception exception)
        {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }

    }










}
