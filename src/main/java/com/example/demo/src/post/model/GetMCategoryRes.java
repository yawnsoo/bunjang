package com.example.demo.src.post.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetMCategoryRes {

    private int large_category_id;
    private String large_category_name;
    private int middle_category_id;
    private String middle_category_name;
}
