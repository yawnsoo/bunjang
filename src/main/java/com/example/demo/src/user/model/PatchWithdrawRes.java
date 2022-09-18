package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class PatchWithdrawRes {
    private int user_id;
    private int status;
    private String reason;

    public PatchWithdrawRes(int user_id, int status) {
        this.user_id = user_id;
        this.status = status;
    }
}
