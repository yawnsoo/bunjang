package com.example.demo.src.account;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.account.model.GetAccountRes;
import com.example.demo.src.account.model.PostAccountReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;


@RestController
@RequestMapping("/account")
public class AccountController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private final AccountProvider accountProvider;
    @Autowired
    private final AccountService accountService;
    @Autowired
    private final JwtService jwtService;

    public AccountController (AccountProvider accountProvider ,AccountService accountService ,JwtService jwtService )
    {
        this.accountProvider = accountProvider;
        this.accountService = accountService;
        this.jwtService = jwtService;
    }


    /**
     * 계좌생성
     * */

    @Transactional
    @ResponseBody
    @PostMapping("/{user_id}")
    public BaseResponse<String> createAccount(@PathVariable("user_id")int user_id , @RequestBody PostAccountReq postAccountReq)
    {

        postAccountReq.setUser_id(user_id);
        //pathvariable로 입력받는 user_id에 대한 validation처리 (활성화된 아이디인지)
        try{
            int user_result = accountProvider.checkUser(user_id);

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
            // 기본계좌로 지정하려고할때 이미 지정된게있는지 체크하고 있으면, 그걸 default 변수를 0으로 바꾸고
            // 지금 만드려는걸 기본계좌로 등록함.
            accountProvider.checkDefault(user_id);

            int result = accountService.createAccount(postAccountReq);
            String aa = "user_account 아이디는 :" + Integer.toString(result);

            return new BaseResponse<>(aa);


        }catch (BaseException exception)
        {
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }
    }


    /**
     * 계좌들 조회 api
     * */

    @ResponseBody
    @GetMapping("/{user_id}")
    public BaseResponse<List<GetAccountRes>> getAccounts(@PathVariable("user_id")int user_id)
    {
        //pathvariable로 입력받는 user_id에 대한 validation처리 (활성화된 아이디인지)
        try{
            int user_result = accountProvider.checkUser(user_id);

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

            List<GetAccountRes> accountRes = accountProvider.getAccounts(user_id);
            return new BaseResponse<>(accountRes);


        }catch (BaseException exception)
        {
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }


    }

    /**
     * 계좌삭제
     * */

    @Transactional
    @ResponseBody
    @DeleteMapping("/delete/{user_id}")
    public BaseResponse<String> deleteAccount (@PathVariable("user_id")int user_id , @RequestParam("user_account_id")int user_account_id )
    {
        //pathvariable로 입력받는 user_id에 대한 validation처리 (활성화된 아이디인지)
        try{
            int user_result = accountProvider.checkUser(user_id);

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
            accountService.deleteAccount(user_account_id,user_id);

            return new BaseResponse<>("완료");


        }catch (BaseException exception)
        {
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }

    }





}
