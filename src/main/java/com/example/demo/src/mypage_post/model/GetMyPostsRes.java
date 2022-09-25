package com.example.demo.src.mypage_post.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetMyPostsRes {

    private int post_id;
    private int price;
    private String title;
    private String content;
    private int safepay;
//    private List<String> imgUrl;
    private String imgUrl;

}
