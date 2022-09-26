package com.example.demo.src.account.model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetAccountRes {
    private int user_account_id;
    private String bank_name;
    private String account_number;
    private String name ;
}
