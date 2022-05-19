package com.capstone3.showbee.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private User user; //일정 추가한 사람
//    @OneToMany(cascade = CascadeType.ALL)
//    private List<Shared> participant;

    @Builder
    public Schedule(Long sId, User user, String category, String stitle, String content, Integer price, Date date, Integer cycle, Boolean shared) {
        this.content = content;
        this.price = price;
        this.date = date;
        this.cycle = cycle;
        this.shared = shared;
        this.stitle = stitle;
        this.user = user;
        this.category = category;
        this.sId = sId;
    }


}
