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


}
