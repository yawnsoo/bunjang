package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostJoinReq {
    private String name;
    private String registration_number;
    private String phone_number;
    private int mobile_carrier_id;
    private String market_name;

}
