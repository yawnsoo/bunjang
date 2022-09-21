package com.example.demo.src.bungaeTalk.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostTalkRoomRes {
    private int room_id;
    private int buyer_id;
    private int seller_id;
}
