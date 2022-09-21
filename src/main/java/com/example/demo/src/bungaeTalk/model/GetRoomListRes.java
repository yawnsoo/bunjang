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
public class GetRoomListRes {
    private int room_id;
    private String other_person_name;
    private String other_person_image;
    private String last_message;
    private Timestamp last_massage_time;

}
