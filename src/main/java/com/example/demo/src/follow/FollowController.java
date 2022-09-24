package com.example.demo.src.follow;

import com.example.demo.src.bungaeTalk.*;

import com.example.demo.src.follow.model.PostFollowReq;
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
@RequestMapping("/follow")
public class FollowController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private final FollowProvider followProvider;
    @Autowired
    private final FollowService followService;
    @Autowired
    private final JwtService jwtService;

    public FollowController (FollowProvider followProvider ,FollowService followService ,JwtService jwtService )
    {
        this.followProvider = followProvider;
        this.followService = followService;
        this.jwtService = jwtService;
    }


    /**
     * 팔로우 , 언팔로우 api
     * */

    @Transactional
    @ResponseBody
    @PostMapping("{user_id}")
    public BaseResponse<String> createFollow (@PathVariable("user_id")int user_id , @RequestBody PostFollowReq postFollowReq)
    {

        //자기자신을 팔로우하는건지도 체크 validation
        if(user_id == postFollowReq.getFollowee_id())
        {
            return new BaseResponse<>(CANT_FOLLOW_MYSELF);
        }

        postFollowReq.setFollwer_id(user_id);

        //pathvariable로 입력받는 user_id에 대한 validation처리 (활성화된 아이디인지)
        try{
            int user_result = followProvider.checkUser(user_id);

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

        //팔로우하려는 상대방도 체크해야함.
        try{
            int user_result = followProvider.checkUser(postFollowReq.getFollowee_id());

            if(user_result ==0)
            {
                return new BaseResponse<>(NOT_EXIST_OPPOSITE_USER);
            }else if(user_result ==-1)
            {
                return new BaseResponse<>(WITHDRAW_OPPOSITE_USER);
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
            int status = followService.createFollow(postFollowReq);
            String result ="오류가 발생하였습니다."; // 기본값은 오류발생으로
            if(status ==0)
            {
                result = "언팔로우하였습니다.";
            }
            if(status == 1)
            {
                result = "팔로우하였습니다.";
            }

            return new BaseResponse<>(result);



        }catch (BaseException exception)
        {
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }

    }


    /**
     * 팔로워 숫자 조회 api // 나를 팔로우 한 사람 수 보여주는 api
     * */
    @ResponseBody
    @GetMapping("/follower/{user_id}")
    public BaseResponse<Integer> followerNumber (@PathVariable ("user_id")int user_id)
    {
        //pathvariable로 입력받는 user_id에 대한 validation처리 (활성화된 아이디인지)
        try{
            int user_result = followProvider.checkUser(user_id);

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

            int result = followProvider.checkFolloerNumber(user_id);
            return new BaseResponse<>(result);



        }catch (BaseException exception)
        {
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }

    }





    /**
     * 팔로워 유저들 정보 조회 // 나를 팔로우 한 유저의 정보 보여주는 api.
     * */




    /**
     * 팔로잉 숫자 조회 api (내가 팔로윙하는 사람들 수 )
     * */
    @ResponseBody
    @GetMapping("/following/{user_id}")
    public BaseResponse<Integer> followingNumber (@PathVariable("user_id")int user_id)
    {
        //pathvariable로 입력받는 user_id에 대한 validation처리 (활성화된 아이디인지)
        try{
            int user_result = followProvider.checkUser(user_id);

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

            int result = followProvider.checkFollowingNumber(user_id);

            return new BaseResponse<>(result);




        }catch (BaseException exception)
        {
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }
    }



}
