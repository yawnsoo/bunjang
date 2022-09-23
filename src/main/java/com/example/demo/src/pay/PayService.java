package com.example.demo.src.pay;

import com.example.demo.config.BaseException;
import com.example.demo.src.bungaeTalk.TalkDao;
import com.example.demo.src.bungaeTalk.TalkProvider;
import com.example.demo.src.bungaeTalk.model.PostTalkMessageReq;
import com.example.demo.src.bungaeTalk.model.PostTalkMessageRes;
import com.example.demo.src.bungaeTalk.model.PostTalkRoomReq;
import com.example.demo.src.bungaeTalk.model.PostTalkRoomRes;
import com.example.demo.src.oauth.model.PostKakaoLoginReq;
import com.example.demo.src.oauth.model.PostKakaoLoginRes;
import com.example.demo.src.pay.model.PostPayHistoryReq;
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
public class PayService {
    private final PayDao payDao;
    private final PayProvider payProvider;
    private final JwtService jwtService;


    @Autowired
    public PayService(PayDao payDao, PayProvider payProvider, JwtService jwtService) {
        this.payDao = payDao;
        this.payProvider = payProvider;
        this.jwtService = jwtService;

    }

    //결제내역 생성 api
    @Transactional
    public PostPayHistoryRes createPaymentDetail(PostPayHistoryReq postPayHistoryReq) throws BaseException
    {
        try{
            PostPayHistoryRes postPayHistoryRes = payDao.createPaymentDetail(postPayHistoryReq);
            return postPayHistoryRes;

        }catch (Exception exception)
        {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
