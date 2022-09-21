package com.example.demo.src.oauth;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.oauth.model.PostKakaoLoginRes;
import com.example.demo.src.user.model.PostLoginReq;
import com.example.demo.src.user.model.PostLoginRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.example.demo.utils.JwtService;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class AuthProvider {

    private final AuthDao authDao;
    private final JwtService jwtService;

    @Autowired
    public AuthProvider(AuthDao authDao , JwtService jwtService)
    {
        this.authDao = authDao;
        this.jwtService = jwtService;

    }





    public void checkScope(String token) throws BaseException { // 동의내역파악

        String reqURL = "https://kapi.kakao.com/v2/user/scopes";

        //access_token을 이용하여 사용자 정보 조회
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
//            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + token); //전송할 header 작성, access_token전송

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //카카오 연동이 된 회원인지 체크
    public int checkKaKaoId(String kakao_id,String name , String birthday) throws BaseException
    {
        try{
            return authDao.checkKakaoId(kakao_id,name,birthday);

        }catch (Exception exception)
        {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }



    //카카오 로그인
    @Transactional
    public PostKakaoLoginRes kakaologin(String kakao_id) throws BaseException
    {
        try{
            int user_id = authDao.kakaoLogin(kakao_id);
            if(user_id ==0) // 카카오연동을 하지 않은회원이므로 카카오고유번호를 등록시켜주고 로그인한다.
            {
                throw new BaseException(NOT_EXIST_USER);
            }
            if(user_id ==-1) // 탈퇴한 회원임을 알린다.
            {
                throw new BaseException(WITHDRAW_USER);
            }
            else{
                String jwt =jwtService.createJwt(user_id);

                PostKakaoLoginRes postKakaoLoginRes = new PostKakaoLoginRes();
                postKakaoLoginRes.setUser_id(user_id);
                postKakaoLoginRes.setJwt(jwt);
                postKakaoLoginRes.setKakao_id(kakao_id);

                return postKakaoLoginRes;
            }


        }
        catch (Exception exception)
        {
            exception.printStackTrace();
            throw exception; // not_exist_user을 날리기위해.
        }


    }






}
