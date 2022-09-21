package com.example.demo.src.bungaeTalk.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostTalkRoomReq {
    private int buyer_id; //postman에서는 빈값으로 전달해도됨. ( pathvariable로 이미 user_id를 받기떄문)
    private int seller_id;
    private String message ; // 맨처음 보낼 메시지
}
