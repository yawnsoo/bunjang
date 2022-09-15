package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetAddressRes {
    private int user_id;
    private int user_address_id;
    private String address_name;
    private String full_address;
    private boolean is_main_address;

}
