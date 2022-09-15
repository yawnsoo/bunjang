package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostUserReq {
    private String login_id;
    private String password;
    private String email;
    private String name;
    private String phone_number;
    private String nickname;
    private String agree_on_mail;
    private String agree_on_sms;
}
