package com.example.demo.src.search;

import com.example.demo.config.BaseException;
import com.example.demo.src.bungaeTalk.TalkDao;
import com.example.demo.src.bungaeTalk.TalkProvider;
import com.example.demo.src.bungaeTalk.model.PostTalkMessageReq;
import com.example.demo.src.bungaeTalk.model.PostTalkMessageRes;
import com.example.demo.src.bungaeTalk.model.PostTalkRoomReq;
import com.example.demo.src.bungaeTalk.model.PostTalkRoomRes;
import com.example.demo.src.follow.FollowDao;
import com.example.demo.src.follow.FollowProvider;
import com.example.demo.src.follow.model.PostFollowReq;
import com.example.demo.src.oauth.model.PostKakaoLoginReq;
import com.example.demo.src.oauth.model.PostKakaoLoginRes;
import com.example.demo.src.pay.model.PostPayHistoryRes;
import com.example.demo.src.point.PointDao;
import com.example.demo.src.point.PointProvider;
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
public class SearchService {

    private final SearchDao searchDao;
    private final SearchProvider searchProvider;
    private final JwtService jwtService;


    @Autowired
    public SearchService(SearchDao searchDao, SearchProvider searchProvider, JwtService jwtService) {
        this.searchDao = searchDao;
        this.searchProvider = searchProvider;
        this.jwtService = jwtService;

    }
}
