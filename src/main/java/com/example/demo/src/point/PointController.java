package com.example.demo.src.point;


import com.example.demo.src.bungaeTalk.*;

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
@RequestMapping("/point")
public class PointController {


    final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private final PointProvider pointProvider;
    @Autowired
    private final PointService pointService;
    @Autowired
    private final JwtService jwtService;

    public PointController (PointProvider pointProvider ,PointService pointService ,JwtService jwtService )
    {
        this.pointProvider = pointProvider;
        this.pointService = pointService;
        this.jwtService = jwtService;
    }



    /**
     * 포인트 내역 생성 api
     */

    @Transactional
    @ResponseBody
    @PostMapping("{user_id}")
    public BaseResponse<PostPointHistoryRes> createPointHistory (@PathVariable("user_id") int user_id , @RequestBody PostPointHistoryReq pointHistoryReq)
    {
        //1. 포인트내역이 없는데 음수는 불가능.
        //맨처음 포인트 변화량은 음수가 될수없음(포인트의 총량은 0또는 양수여야함)
        try
        {
            if(pointProvider.checkPointIDExist(user_id) != 1)
            {
                return new BaseResponse<>(CANT_START_MINUS);
            }

        }catch (BaseException exception)
        {
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }

        //2. 포인트의 총량보다 큰 음수는 불가능. (포인트의 총량이 마이너스이면 안되니까 ++ 가지고있는 포인트보다 더쓸수없음)

        try
        {
            int point = pointProvider.checkUserPoint(user_id);
            int amount = pointHistoryReq.getAmount();
            int result = point + amount;
            System.out.println("point = " + point);
            System.out.println("amount = " + amount);
            System.out.println("result = " + result);
            if(result < 0) //현재 포인트양보다 큰 양을 사용하려고한다면 안됨.
            {
                return new BaseResponse<>(OVER_POINT);
            }

        }catch (BaseException exception)
        {
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }



        pointHistoryReq.setUser_id(user_id);

        //pathvariable로 입력받는 user_id에 대한 validation처리 (활성화된 아이디인지)
        try{
            int user_result = pointProvider.checkUser(user_id);

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


            PostPointHistoryRes postPointHistoryRes =  pointService.createPointHistory(pointHistoryReq);
            return new BaseResponse<>(postPointHistoryRes);





        }catch (BaseException exception)
        {
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }

    }


    /**
     * 유저의 번개포인트 총량 조회 ( 사용자의 총 번개포인트 조회)
     * */

    @ResponseBody
    @GetMapping("{user_id}")
    public BaseResponse<Integer> getUserPoint (@PathVariable("user_id")int user_id)
    {
        //pathvariable로 입력받는 user_id에 대한 validation처리 (활성화된 아이디인지)

        try{
            int user_result = pointProvider.checkUser(user_id);

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

            int result = pointProvider.checkUserPoint(user_id);

            return new BaseResponse<>(result);



        }catch (BaseException exception)
        {
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }


    }





}
