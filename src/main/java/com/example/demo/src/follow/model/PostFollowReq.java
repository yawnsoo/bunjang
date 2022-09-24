package com.example.demo.src.follow.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostFollowReq {
    private int followee_id; // 팔로우 된 사람.
    private int follwer_id; //팔로우한사람


}
