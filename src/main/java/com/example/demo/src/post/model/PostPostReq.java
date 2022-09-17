package com.example.demo.src.post.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostPostReq {
    private String title;
    private int price;
    private String content;
    private int count;
    private int is_exchangable;
    private int safepay;
    private int delivery_fee;
    private int pcondition;
}


/*
*
* 1. 카테고리 분류 중 중분류까지만 존재하는 경우도 있음 -> post의 소분류id null 설정 필요
*
* 2. 카테고리 입력시 카테고리 조회 api랑 연동 시켜야할듯... 근데 어떻게..? # body에 입력하는 방식이 아닌 선택하는 방식으로 구현
*
*
*
*
*
* */