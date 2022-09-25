package com.example.demo.src.home.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetHomePostsRes {

    private int post_id;
    private String imgUrl;
    private int price;
    private String title;
    private int safepay;
    private String region;
    private String created_at;
    private int jjim;
}
