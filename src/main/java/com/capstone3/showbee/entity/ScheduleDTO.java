package com.capstone3.showbee.entity;

import lombok.Builder;

import java.util.Date;

public class ScheduleDTO {
    private Long sId;

    private String stitle;
    private String content;
    private Integer price;
    private Date date;
    private Boolean shared;
    private Integer cycle;

    public Schedule toEntity(User user){
        return Schedule.builder()
                .content(content).user(user).price(price).date(date).cycle(cycle).shared(shared).stitle(stitle)
                .build();
    }

    @Builder
    public ScheduleDTO(String stitle, String content, Integer price, Date date, Integer cycle, Boolean shared) {
        this.content = content;
        this.price = price;
        this.date = date;
        this.cycle = cycle;
        this.shared = shared;
        this.stitle = stitle;
    }
}
