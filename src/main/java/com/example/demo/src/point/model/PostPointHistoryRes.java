package com.example.demo.src.point.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostPointHistoryRes {

    private int point_history_id;
    private int user_id;
    private int amount;

}
