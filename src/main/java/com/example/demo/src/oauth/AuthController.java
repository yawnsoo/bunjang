package com.example.demo.src.oauth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/oauth")
public class AuthController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    public AuthController()
    {

    }

    //클라이언트에서 해야하는부분(인가 코드를 받고 그 인가코드를 이용하여 엑세스토큰을 받는부분)
    //토큰을 이용하여 정보를 받아오는것을 테스트하기 위해 진행중.
    @ResponseBody
    @GetMapping("/kakao")
    public void kakaoCallback(@RequestParam String code)
    {

    }




}
