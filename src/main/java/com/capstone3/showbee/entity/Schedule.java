package com.capstone3.showbee.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Builder
@Getter
@Entity
@Table(name = "Schedule")
@AllArgsConstructor
@NoArgsConstructor
public class Schedule {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleId;
    private String content;
    private Integer price;
    private Date date;
    private Boolean shared;
    private Integer cycle;



}
