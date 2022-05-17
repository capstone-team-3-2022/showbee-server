package com.capstone3.showbee.entity;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.lang.module.FindException;
import java.util.Date;
import java.util.List;

@Getter
@Entity
@Table(name = "Schedule")
@NoArgsConstructor
public class Schedule {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sId;

    private String stitle;
    private String content;
    private Integer price;
    private Date date;
    private Boolean shared;
    private Integer cycle;
    private String category;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user; //일정 추가한 사람
    @ManyToMany(fetch = FetchType.LAZY)
    private List<User> participant;

    @Builder
    public Schedule(User user, String category, String stitle, String content, Integer price, Date date, Integer cycle, Boolean shared) {
        this.content = content;
        this.price = price;
        this.date = date;
        this.cycle = cycle;
        this.shared = shared;
        this.stitle = stitle;
        this.user = user;
        this.category = category;
    }

    public void updateContent(String content){
        this.content = content;
    }


}
