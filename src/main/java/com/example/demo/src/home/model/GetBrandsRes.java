package com.example.demo.src.home.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetBrandsRes {
    private int brand_id;
    private String name_kr;
    private String name_eng;
    private int status; //brand_follow.status
}
