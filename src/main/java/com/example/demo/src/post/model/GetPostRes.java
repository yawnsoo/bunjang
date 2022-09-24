package com.example.demo.src.post.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetPostRes {

    private int post_id;
    private int price;
    private String title;
    private int safepay;
    private String imgUrl;
}
