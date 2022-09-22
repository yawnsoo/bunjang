package com.example.demo.src.post.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostPostAssemble {

    private PostPostReq postPostReq;
    private PostPhotoReq postPhotoReq;
    private PostTagReq postTagReq;

}
