package com.example.demo.src.oauth;

import com.example.demo.config.BaseException;
import com.example.demo.src.oauth.model.PostKakaoLoginRes;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class AuthService {

    private final AuthProvider authProvider;

    @Autowired
    public AuthService ( AuthProvider authProvider)
    {
        this.authProvider = authProvider;
    }


    public String getKakaoAccessToken (String code) { //토큰을 받아오는 함수.
        String access_Token = "";
        String refresh_Token = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //POST 요청을 위해 기본값이 false인 setDoOutput을 true로
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            //POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=ffb7e083a50cae0f26333279f9ad2dd3"); // TODO REST_API_KEY 입력
            sb.append("&redirect_uri=http://localhost:9001/oauth/kakao"); // TODO 인가코드 받은 redirect_uri 입력
            sb.append("&code=" + code);
            bw.write(sb.toString());
            bw.flush();

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

            //Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            access_Token = element.getAsJsonObject().get("access_token").getAsString();
            refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();

            System.out.println("access_token : " + access_Token);
            System.out.println("refresh_token : " + refresh_Token);

            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return access_Token;
    }


    @Transactional
    public PostKakaoLoginRes loginKakao(String token) throws BaseException { //받은 토큰을 이용하여 카카오서버에서 회원정보를 받아온다.
        //받아온 회원정보(카카오 고유아이디,닉네임(이름),생일) 를 내 DB서버 이용자 정보와 비교하여
        // 1. 카카오 로그인한 기록이있는 유저 ( 카카오 고유 아이디로 내 DB검색시 나온다.) -> 로그인 시켜준다.
        // 2. 일반회원가입은 했지만 카카오로 로그인한적은없는 유저 -> 카카오고유아이디를 추가해주고 로그인 시켜준다.
        // (카카오에서 받아온 정보로 내 DB검색시 나온다 -> 일반회원가입한후 카카오로그인은 처음시도하는 회원)
        // 3. 일반회원가입도 하지않은 유저 -> 일반회원가입 화면으로 이동시켜서 일반회원가입을 유도한다.
        // (고유아이디,카카오서버에서 받아온정보 둘을 이용해서 검색해봐도 우리 회원이 아니라는뜻 -> validation처리)

        String reqURL = "https://kapi.kakao.com/v2/user/me";
        String id="";
        String name="";
        String birthday ="";
        String email = "";

        //access_token을 이용하여 사용자 정보 조회
        try { // 이 부분은 토큰을 이용하여 정보를 받아오는 부분입니다.
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
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
//            받아오는 Response를 보고싶으면 주석해제
            System.out.println("response body : " + result);

            //Gson 라이브러리로 JSON파싱
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            id = element.getAsJsonObject().get("id").getAsString();
//            boolean hasEmail = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("has_email").getAsBoolean();
//            boolean hasBirthday = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("has_birthday").getAsBoolean();

            name = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("profile").getAsJsonObject().get("nickname").getAsString();
//            if(hasEmail){
//                email = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("email").getAsString();
//            }
//            if(hasBirthday){
//                birthday = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("birthday").getAsString();
//            }



            br.close();





        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("ioException오류");
        }

        try{ // 이부분은 카카오 로그인 해주는부분 , 일반회원 카카오 연동시켜주고 카카오 로그인시켜주는 부분입니다.

            System.out.println("id : " + id);
            System.out.println("name = " + name);
            System.out.println("email : " + email);
            System.out.println("birthday = " + birthday);

            //1. 카카오 로그인한 기록이있는 유저 ( 카카오 고유 아이디로 내 DB검색시 나온다.) -> 로그인 시켜준다.
            // 2. 일반회원가입은 했지만 카카오로 로그인한적은없는 유저 -> 카카오고유아이디를 추가해주고 로그인 시켜준다. (카카오연동)
            // (카카오에서 받아온 정보로 내 DB검색시 나온다 -> 일반회원가입한후 카카오로그인은 처음시도하는 회원)
            if(authProvider.checkKaKaoId(id,name,birthday) != 1) // 카카오 로그인한 기록이있으면 해당 아이디로 로그인.
            {
                // 3. 일반회원가입도 하지않은 유저 -> 일반회원가입 화면으로 이동시켜서 일반회원가입을 유도한다.
                //일반회원가입조차 되어있지않으므로 존재하지않는 회원이라는 validation처리
                throw new BaseException(NOT_EXIST_USER);

            }

            //연동되어있으므로 로그인시켜줌
            //로그인함수
            return authProvider.kakaologin(id);
        }
        catch (Exception exception) {
            exception.printStackTrace();
            throw exception;
        }

    }




}
