package com.example.demo.src.oauth;

import com.example.demo.src.oauth.model.PostKakaoLoginReq;
import com.example.demo.src.oauth.model.PostKakaoLoginRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;


@RestController
@RequestMapping("/oauth")
public class AuthController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final AuthService authService;
    @Autowired
    private final AuthProvider authProvider;


    public AuthController(AuthService authService , AuthProvider authProvider)
    {
        this.authService = authService;
        this.authProvider =authProvider;
    }

    //클라이언트에서 해야하는부분(인가 코드를 받고 그 인가코드를 이용하여 엑세스토큰을 받는부분)
    //토큰을 이용하여 정보를 받아오는것을 테스트하기 위해 진행중.
    @ResponseBody
    @GetMapping("/kakao")
    public void kakaoCallback(@RequestParam String code) //인가코드를 받기위한부분(클라이언트에서 받아야할부분)
    {
        System.out.println("code = " + code);

        String token = authService.getKakaoAccessToken(code); // 엑세스 토큰 받고.
        System.out.println("token = " + token);
//        try{
//            authService.loginKakao(token); //카카오연동회원찾아서 로그인시켜주거나 일반회원을 카카오 연동시켜서 로그인시켜주기
//            authProvider.checkScope(token);
//
//        }
//        catch (BaseException exception)
//        {
//            exception.printStackTrace();
////            return new BaseResponse<>(exception.getStatus());
//        }


    }

    @Transactional
    @ResponseBody
    @PostMapping("/kakao/login")
    BaseResponse<PostKakaoLoginRes> kakaoLogin(@RequestBody PostKakaoLoginReq postKakaoLoginReq) // 토큰을 이용한 카카오로그인
            //또는 카카오 연동해주고 로그인.
    {
        String token = postKakaoLoginReq.getAccess_token();
//          동의항목 체크하는부분.
//            try{
//            authProvider.checkScope(token);
//
//        }
//        catch (BaseException exception)
//        {
//            exception.printStackTrace();
//            return new BaseResponse<>(exception.getStatus());
//        }

        try{
            PostKakaoLoginRes postKakaoLoginRes = authService.loginKakao(token);
            return new BaseResponse<>(postKakaoLoginRes);
        }
        catch (BaseException exception)
        {
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }


    }




}
