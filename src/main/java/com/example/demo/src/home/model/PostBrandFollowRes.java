package com.example.demo.src.home.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostBrandFollowRes {

    private int user_id;
    private int brand_id;
    private int status;
}
