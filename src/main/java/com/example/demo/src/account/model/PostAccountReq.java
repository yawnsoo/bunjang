package com.example.demo.src.account.model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostAccountReq {
    private String name;
    private int bank_id;
    private String account_number;
    private int is_default_account;
    private int user_id;
}
