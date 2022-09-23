package com.example.demo.src.point.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor



public class PostPointHistoryReq {
    private int user_id; // pathvariable로 받으므로 body로 받을필요없음.
    private int amount ; //포인트 변화량 , 음수도 가능

}
