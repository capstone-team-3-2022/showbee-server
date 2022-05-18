package com.capstone3.showbee.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinancialDTO {
    private Long fid;
    private String content;
    private String category;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;
    private int price;

    public Financial toEntity(User user){
        return Financial.builder()
                .category(category).user(user).price(price).date(date).content(content).fid(fid)
                .build();
    }

//    @Builder
//    public FinancialDTO(Date date, int price, String content, String category){
//        this.category = category;
//        this.date = date;
//        this.content = content;
//        this.price = price;
//    }


}
