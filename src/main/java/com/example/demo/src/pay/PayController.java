package com.example.demo.src.pay;


import com.example.demo.src.bungaeTalk.*;

import com.example.demo.src.pay.model.PostPayHistoryReq;
import com.example.demo.src.pay.model.PostPayHistoryRes;
import com.example.demo.src.point.PointProvider;
import com.example.demo.src.point.PointService;
import com.example.demo.src.point.model.PostPointHistoryReq;
import com.example.demo.src.point.model.PostPointHistoryRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;


@RestController
@RequestMapping("/pay")
public class PayController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private final PayProvider payProvider;
    @Autowired
    private final PayService payService;
    @Autowired
    private final JwtService jwtService;

    public PayController (PayProvider payProvider ,PayService payService ,JwtService jwtService )
    {
        this.payProvider = payProvider;
        this.payService = payService;
        this.jwtService = jwtService;
    }



    /**
     * 결제내역 생성 api
     * */
    @Transactional
    @ResponseBody
    @PostMapping("{user_id}")
    public BaseResponse<PostPayHistoryRes> createPaymentDetail (@PathVariable("user_id") int user_id , @RequestBody PostPayHistoryReq postPayHistoryReq)
    {
        postPayHistoryReq.setBuyer_id(user_id);

        //pathvariable로 입력받는 user_id에 대한 validation처리 (활성화된 아이디인지)
        try{
            int user_result = payProvider.checkUser(user_id);

            if(user_result ==0)
            {
                return new BaseResponse<>(NOT_EXIST_USER);
            }else if(user_result ==-1)
            {
                return new BaseResponse<>(WITHDRAW_USER);
            }
        }catch (BaseException exception)
        {
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }

        try{

            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(user_id != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            PostPayHistoryRes postPayHistoryRes = payService.createPaymentDetail(postPayHistoryReq);
            return new BaseResponse<>(postPayHistoryRes);




        }catch (BaseException exception)
        {
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }

    }


}
