package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class PostAddressReq {
    private int user_id;
    private String address_name;
    private String full_address;
    private boolean is_main_address;



}
