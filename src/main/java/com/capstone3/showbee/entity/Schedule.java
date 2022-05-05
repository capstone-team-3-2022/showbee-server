package com.capstone3.showbee.entity;

import lombok.*;

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
    @ManyToOne(fetch = FetchType.LAZY)
    private User user; //일정 추가한 사람
//    @ManyToMany(fetch = FetchType.EAGER)
//    private List<User> participant;

//    @OneToMany
//    private List<Shared> sharedsch;

    @Builder
    public Schedule(User user, String stitle, String content, Integer price, Date date, Integer cycle, Boolean shared) {
        this.content = content;
        this.price = price;
        this.date = date;
        this.cycle = cycle;
        this.shared = shared;
        this.stitle = stitle;
        this.user = user;
    }

    public void updateContent(String content){
        this.content = content;
    }


}
