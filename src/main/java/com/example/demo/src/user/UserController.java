package com.example.demo.src.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;
//git 연동 실험용 주석
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




    public UserController(UserProvider userProvider, UserService userService, JwtService jwtService){
        this.userProvider = userProvider;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    /**
     * 회원 조회 API
     * [GET] /users
     * 회원 번호 및 이메일 검색 조회 API
     * [GET] /users? Email=
     * @return BaseResponse<List<GetUserRes>>
     */
    //Query String
    @ResponseBody
    @GetMapping("") // (GET) 127.0.0.1:9000/app/users
    public BaseResponse<List<GetUserRes>> getUsers(@RequestParam(required = false) String email) {
        try{
            if(email == null){
                List<GetUserRes> getUsersRes = userProvider.getUsers();
                return new BaseResponse<>(getUsersRes);
            }
            // Get Users
            List<GetUserRes> getUsersRes = userProvider.getUsersByEmail(email);
            return new BaseResponse<>(getUsersRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 회원 1명 조회 API
     * [GET] /users/:user_id
     * @return BaseResponse<GetUserRes>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/{user_id}") // (GET) 127.0.0.1:9000/app/users/:user_id
    public BaseResponse<GetUserRes> getUser(@PathVariable("user_id") int user_id) {
        // Get Users
        try{
            GetUserRes getUserRes = userProvider.getUser(user_id);
            return new BaseResponse<>(getUserRes);
        } catch(BaseException exception){
            exception.printStackTrace();
            return new BaseResponse<>((exception.getStatus()));
        }

    }
    /**
     * 회원가입 API
     * [POST] /users
     * @return BaseResponse<PostUserRes>
     */
    // Body
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostUserRes> createUser(@RequestBody PostUserReq postUserReq) {
        // TODO: email 관련한 짧은 validation 예시입니다. 그 외 더 부가적으로 추가해주세요!
        if(postUserReq.getEmail() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
        }
        //이메일 정규표현
        if(!isRegexEmail(postUserReq.getEmail())){
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }
        if(postUserReq.getLogin_id() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_LOGIN_ID);
        }
        if(postUserReq.getPassword() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_PASSWORD);
        }
        if(postUserReq.getName() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_NAME);
        }
        if(postUserReq.getPhone_number() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_PHONE_NUMBER);
        }
        if(postUserReq.getNickname() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_NICKNAME);
        }


        try{
            PostUserRes postUserRes = userService.createUser(postUserReq);
            return new BaseResponse<>(postUserRes);
        } catch(BaseException exception){
            exception.printStackTrace();
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     * 로그인 API
     * [POST] /users/logIn
     * @return BaseResponse<PostLoginRes>
     */
    @ResponseBody
    @PostMapping("/login")
    public BaseResponse<PostLoginRes> logIn(@RequestBody PostLoginReq postLoginReq){
        try{
            // TODO: 로그인 값들에 대한 형식적인 validatin 처리해주셔야합니다!
            // TODO: 유저의 status ex) 비활성화된 유저, 탈퇴한 유저 등을 관리해주고 있다면 해당 부분에 대한 validation 처리도 해주셔야합니다.

            if(postLoginReq.getLogin_id() == null) // 아이디 미입력
            {
             // System.out.println("아이디가 입력되지않았습니다.");
              return new BaseResponse<>(POST_USERS_EMPTY_LOGIN_ID);
            }
            if(postLoginReq.getPassword() == null) // 비밀번호 미입력
            {
                return new BaseResponse<>(POST_USERS_EMPTY_PASSWORD);
            }


            PostLoginRes postLoginRes = userProvider.logIn(postLoginReq);

            if(!postLoginRes.isStatus()) // status가 false라면
            {
                return new BaseResponse<>(Withdrawal_USER);
            }

            return new BaseResponse<>(postLoginRes);
        } catch (BaseException exception){
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 유저정보변경 API
     * [PATCH] /users/:userIdx
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{user_id}")
    public BaseResponse<String> modifyUserInfo(@PathVariable("user_id") int user_id, @RequestBody PatchUserReq patchUserReq) {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(user_id != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            //같다면 유저네임 변경
           // PatchUserReq patchUserReq = new PatchUserReq(user_id,user.getName());

            String result = "";
            patchUserReq.setUser_id(user_id); // user아이디 추가해줌.
            if(patchUserReq.getNickname() != null)
            {
                userService.modifyUserNickName(patchUserReq);
                result += "닉네임 변경완료 ";
            }
            if(patchUserReq.getProfile_Image_url() != null)
            {
                userService.modifyUserProfile_Image_url(patchUserReq);
                result += "이미지 변경완료 ";
            }
            if(patchUserReq.getPassword() != null)
            {
                userService.modifyUserPassWord(patchUserReq);
                result += "비밀번호 변경완료 ";
            }
            if(patchUserReq.getPhone_number() != null)
            {
                userService.modifyUserPhone_number(patchUserReq);
                result += "전화번호 변경완료 ";
            }
            if(patchUserReq.getAgree_on_mail() != null)
            {
                userService.modifyUserAgree_on_mail(patchUserReq);
                result += "메일수신여부 변경완료 ";
            }
            if(patchUserReq.getAgree_on_sms() != null)
            {
                userService.modifyUserAgree_on_sms(patchUserReq);
                result += "문자수신여부 변경완료 ";
            }

         return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     * 주소등록 API
     * [POST] /users/address
     * @return BaseResponse<PostLoginRes>
     */
    @ResponseBody
    @PostMapping("/address/{user_id}")
    public BaseResponse<PostAddressRes> SetAddress(@PathVariable("user_id") int user_id, @RequestBody PostAddressReq postAddressReq){
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(user_id != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            postAddressReq.setUser_id(user_id);

            PostAddressRes postAddressRes = userService.createAddress(postAddressReq);
            return new BaseResponse<>(postAddressRes);

        } catch (BaseException exception){
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }
    }
    //자신의 주소 불러오기
    @ResponseBody
    @GetMapping("/address/{user_id}") // (GET)
    public BaseResponse<List<GetAddressRes>> getAddress(@PathVariable("user_id") int user_id) {
        // Get Users
        try{
            List<GetAddressRes> getAddressRes = userProvider.getAddress(user_id);
            return new BaseResponse<>(getAddressRes);
        } catch(BaseException exception){
            exception.printStackTrace();
            return new BaseResponse<>((exception.getStatus()));
        }

    }
    //회원탈퇴
    @ResponseBody
    @DeleteMapping("/{user_id}") // (DELETE)
    public BaseResponse<String> deleteUser(@PathVariable("user_id") int user_id)
    {
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인 , 본인만 탈퇴가능하므로
            if(user_id != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            userService.deleteUser(user_id);

            String result ="유저번호 : "+ Integer.toString(user_id) +" 탈퇴";

            return new BaseResponse<>(result);
        } catch (BaseException exception){
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }

    }

    //주소삭제 , 주소아이디를 이용
    @ResponseBody
    @DeleteMapping("/address/{user_address_id}") // (DELETE)
    public BaseResponse<String> deleteAddress(@PathVariable("user_address_id") int user_address_id)
    {
        try {
            userService.deleteAddress(user_address_id);
            String result ="주소번호 : "+ Integer.toString(user_address_id) +" 삭제";

            return new BaseResponse<>(result);
        }catch (BaseException exception)
        {
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }


    }


}
