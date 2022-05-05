package com.capstone3.showbee.entity;

import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class FinancialDTO {
    private String content;
    private String category;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;
    private int price;

    public Financial toEntity(User user){
        return Financial.builder()
                .category(category).user(user).price(price).date(date).content(content)
                .build();
    }

    @Builder
    public FinancialDTO(Date date, int price, String content, String category){
        this.category = category;
        this.date = date;
        this.content = content;
        this.price = price;
    }


}
