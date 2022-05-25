package com.capstone3.showbee.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.lang.module.FindException;
import java.time.LocalDate;
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
    private LocalDate date;
    private Boolean shared;
    private Integer cycle;
    private String category;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User user; //일정 추가한 사람
    private boolean inoutcome;
//    @OneToMany(cascade = CascadeType.ALL)
//    private List<Shared> participant;

    @Builder
    public Schedule(boolean inoutcome, Long sId, User user, String category, String stitle, String content, Integer price, LocalDate date, Integer cycle, Boolean shared) {
        this.content = content;
        this.price = price;
        this.date = date;
        this.cycle = cycle;
        this.shared = shared;
        this.stitle = stitle;
        this.user = user;
        this.category = category;
        this.sId = sId;
        this.inoutcome = inoutcome;
    }

    public boolean getInoutcome(){
        return this.inoutcome;
    }


}
