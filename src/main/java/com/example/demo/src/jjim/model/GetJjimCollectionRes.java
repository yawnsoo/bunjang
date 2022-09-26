package com.example.demo.src.jjim.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetJjimCollectionRes {

    private int jjim_colection_id;
    private String img_url;
    private String name;
    private int countProduct;
}
