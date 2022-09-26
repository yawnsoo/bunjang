package com.example.demo.src.user;

import com.example.demo.src.s3.S3UploadController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.Base64;
import java.util.regex.Pattern;
import java.util.List;


import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;

@RestController
@RequestMapping("/app/users")
public class UserController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final UserProvider userProvider;
    @Autowired
    private final UserService userService;
    @Autowired
    private final JwtService jwtService;

    @Autowired
    private final S3UploadController s3UploadController;




    public UserController(UserProvider userProvider, UserService userService, JwtService jwtService ,S3UploadController s3UploadController){
        this.userProvider = userProvider;
        this.userService = userService;
        this.jwtService = jwtService;
        this.s3UploadController = s3UploadController;
    }

    /**일반회원가입 POST */
    @Transactional
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostJoinRes> createUser(@RequestBody PostJoinReq postJoinReq)
    {




        if(postJoinReq.getMarket_name() == null){ // 상점이름 빈칸체크
            return new BaseResponse<>(USERS_EMPTY_USER_MARKET_NAME);
        }

        //상점이름 한글,영어,숫자만가능에 관련한 validation
        String market_name = postJoinReq.getMarket_name();
        boolean market_name_result = Pattern.matches("^[a-zA-Z가-힣0-9]*$",market_name);
        if(!market_name_result)
        {
            return new BaseResponse<>(USERS_WRONG_MARKET_NAME);
        }

        //상점이름 중복 관련 validation
        try{
            if(userProvider.checkMarketName(market_name) == 1){
                return new BaseResponse<>(USERS_EXIST_MARKET_NAME);
            }
        }
        catch (BaseException exception)
        {
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }


        try{
            PostJoinRes postJoinRes = userService.createUser(postJoinReq);
            return new BaseResponse<>(postJoinRes);
        }catch (BaseException exception)
        {
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());

        }

    }

    /** 일반로그인 [POST] */
    @ResponseBody
    @PostMapping("/login")
    public BaseResponse<PostLoginRes> login(@RequestBody PostLoginReq postLoginReq)
    {
        //입력값이 비어있는 경우에 대한 각각의 validation 처리
        if(postLoginReq.getName() == null){ // 이름 빈칸
            return new BaseResponse<>(USERS_EMPTY_USER_NAME);
        }
        if(postLoginReq.getPhone_number() == null){ //전화번호 빈칸
            return new BaseResponse<>(USERS_EMPTY_USER_PHONE_NUMBER);
        }
        if(postLoginReq.getMobile_carrier_id() == 0){ //int형의 초기값은 0이므로 , 선택이 안된건 0 , 통신사 빈칸
            return new BaseResponse<>(USERS_EMPTY_USER_MOBILE_CARRIER_ID);
        }
        if(postLoginReq.getRegistration_number() == null){ // 주민등록번호 빈칸
            return new BaseResponse<>(USERS_EMPTY_USER_REGISTRATION_NUMBER);
        }
        //인증번호 validation 고정적인 번호 1234가 아니면 안됨
        if(postLoginReq.getAuth_number() != 1234)
        {
            return new BaseResponse<>(WRONG_AUTH_NUMBER);
        }

        //잘못된 입력의 validation
        //이름에 숫자 ,이모티콘 들어가면안됨 ( 한글과 영어만 들어간지 )validation 정규표현식이용
        String temp = postLoginReq.getName();
        boolean result = Pattern.matches("^[a-zA-Z가-힣]*$",temp);
        if(!result){ // 이름에 영어 또는 한글만 들어간게 아니라면
            return new BaseResponse<>(USERS_WRONG_USER_NAME);
        }

        //유효한주민등록번호인지 체크 ( 월,일 숫자체크해야함) -> 스킵

        //휴대폰번호 길이는 초과못하게 클라이언트에서?
        //휴대폰번호 vaildation  -> 맨앞이 010으로시작하는지 체크 (11자)
        String phone_number = postLoginReq.getPhone_number();
        String phone_temp = phone_number.substring(0,3);
        if(!phone_temp.equals("010"))
        {
            return new BaseResponse<>(USERS_WRONG_PHONE_NUMBER);
        }


        try{

            PostLoginRes postLoginRes = userProvider.login(postLoginReq);
            return new BaseResponse<>(postLoginRes);


        }catch (BaseException exception)
        {
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }

    }

    /** 회원탈퇴
     * - status를 0으로, 탈퇴이유 추가 */
    @Transactional
    @ResponseBody
    @PatchMapping("/withdraw/{user_id}")
    public BaseResponse<PatchWithdrawRes> withdraw(@PathVariable("user_id")int user_id , @RequestBody PatchWithdrawReq patchWithdrawReq)
    {
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인 , 본인만 탈퇴가능하므로
            if(user_id != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            PatchWithdrawRes patchWithdrawRes = userService.withdraw(user_id,patchWithdrawReq);

            return new BaseResponse<>(patchWithdrawRes);



        }catch (BaseException exception)
        {
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }

    }
    /**
     * 상점정보 수정 API [PATCH]
    **/

    @Transactional
    @ResponseBody
    @PatchMapping(value ="/revise/{user_id}")
    public BaseResponse<String> reviseUserInfo(@PathVariable("user_id")int user_id, @RequestBody PatchReviseReq patchReviseReq) throws Exception
    {
        String market_name = patchReviseReq.getMarket_name();
        String imgUrl;
        if(patchReviseReq.getEncoded_image() != null)
        {
            String base64EncodedFile = patchReviseReq.getEncoded_image();
            byte[] decodedFile = Base64.getMimeDecoder().decode(base64EncodedFile);
            imgUrl = s3UploadController.upload2(decodedFile);
            patchReviseReq.setImg_url(imgUrl);
        }

        // 상점이름 빈칸체크 validation
        if(patchReviseReq.getMarket_name() == null){
            return new BaseResponse<>(USERS_EMPTY_USER_MARKET_NAME);
        }

        //상점이름 한글,영어,숫자만가능에 관련한 validation
        boolean market_name_result = Pattern.matches("^[a-zA-Z가-힣0-9]*$",market_name);
        if(!market_name_result)
        {
            return new BaseResponse<>(USERS_WRONG_MARKET_NAME);
        }


        //상점이름 중복 관련 validation
        try{
            if(userProvider.checkMarketName2(market_name,user_id) == 1)
            {//원래상점이름을 전달했는지 체크 하고 아니라면 중복체크

                if(userProvider.checkMarketName(patchReviseReq.getMarket_name()) == 1){
                    return new BaseResponse<>(USERS_EXIST_MARKET_NAME);
                }
            }

        }
        catch (BaseException exception)
        {
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }



        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인 , 본인만 정보수정가능하므로
            if(user_id != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            String result = userService.reviseUserInfo(user_id,patchReviseReq);
            return new BaseResponse<>(result);

        }catch (BaseException exception)
        {
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }

    }


    /**
     * 특정 유저 정보 조회
     * **/
    @ResponseBody
    @GetMapping({"/{user_id}"})
    public BaseResponse<GetUserInfoRes> getUser (@PathVariable("user_id")int user_id)
    {
        //pathvariable로 입력받는 user_id에 대한 validation처리 (활성화된 아이디인지)
        try{
            int user_result = userProvider.checkUser(user_id);

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
            GetUserInfoRes getUserInfoRes = userProvider.getUser(user_id);

            return new BaseResponse<>(getUserInfoRes);



        }catch (BaseException exception)
        {
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }

    }















}
