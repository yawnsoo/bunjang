package com.example.demo.src.pay.model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostPayHistoryRes {
    private int payment_detail_id;
    private int buyer_id;
    private int post_id;
    private int used_point;
    private int total_price;
    private String method;
}
