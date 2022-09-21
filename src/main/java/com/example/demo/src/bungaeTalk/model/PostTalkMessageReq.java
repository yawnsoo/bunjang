package com.example.demo.src.bungaeTalk.model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostTalkMessageReq {
    private int user_id;
    private int room_id;
    private String message;
}
