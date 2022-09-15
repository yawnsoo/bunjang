package com.example.demo.src.user.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserRes {
    private int user_id;
    private String login_id;
    private String password;
    private String email;
    private String profile_Image_url;
    private String name;
    private String phone_number;
    private String agree_on_mail;
    private String agree_on_sms;
    private int point;
    private String create_at;
    private String updated_at;
    private boolean status;
    private String nickname;

}
