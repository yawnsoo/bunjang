package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchUserReq {
    private int user_id;
    private String nickname;
    private String profile_Image_url;
    private String password;
    private String phone_number;
    private String agree_on_mail;
    private String agree_on_sms;

}
