package com.capstone3.showbee.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MonthlySchedule {

    private long sid;
    private int price;
    private String stitle;
}
