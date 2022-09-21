package com.example.demo.src.bungaeTalk.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetMessageRes {
    private int sender_id;
    private String message;
    private Timestamp time;


}
