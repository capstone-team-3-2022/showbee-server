package com.capstone3.showbee.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Getter
@Setter
@Builder
public class MonthlyFinancial {
    private long fid;
    private int price;
    private String category;
    private String content;
    private boolean inoutcome;

//    @Builder
//    public MonthlyFinancial(long fid, int price, String category, String content, boolean inoutcome){
//        this.category = category;
//        this.fid = fid;
//        this.price = price;
//        this.content = content;
//        this.inoutcome = inoutcome;
//    }

}
