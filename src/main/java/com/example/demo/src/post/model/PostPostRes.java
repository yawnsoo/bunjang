package com.example.demo.src.post.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PostPostRes {

    private int post_id;
    private List<String> imgUrls;
    private String title;
    private String region;
    private String created_at;
    private int category_large;
    private int category_middle;
    private int category_small;
    private List<String> tags;
    private int price;
    private String content;
    private int count;
    private int is_exchangable;
    private int safepay;
    private int delivery_fee;
    private int pcondition;
}
