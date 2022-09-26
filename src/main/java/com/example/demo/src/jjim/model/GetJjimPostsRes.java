package com.example.demo.src.jjim.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetJjimPostsRes {
    private int post_id;
    private String imgUrl;
    private int safepay;
    private String title;
    private int price;
    private String market_name;
    private String created_at;
}
