package com.example.demo.src.post.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostPhotoReq {

    private int post_id;
    private String image_path;

}


/*
*
* {
    "image_path" : "testuri-testuri-testuri-testuri-testuri-testuri-testuri-testuri"
}
*
* */