package com.example.demo.src.jjim.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetJjimCollectionPostRes {
    private int post_id;
    private String imgUrl;
    private int safepay;
    private String title;
    private int price;
    private String market_name;
}
