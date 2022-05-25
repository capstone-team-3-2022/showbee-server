package com.capstone3.showbee.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Table(name="financial")
@NoArgsConstructor
@Setter
public class Financial {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fid;
    private int price;
    private LocalDate date;
    private String content;
    private String category;
    private String bank;
    private String memo;
    private boolean inoutcome;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;

    public boolean getInoutcome(){
        return inoutcome;
    }

    @Builder
    public Financial(LocalDate date, int price, String content, String category, User user, Long fid, String bank, String memo, boolean inoutcome){
        this.category = category;
        this.date = date;
        this.content = content;
        this.user = user;
        this.price = price;
        this.fid = fid;
        this.memo = memo;
        this.bank = bank;
        this.inoutcome = inoutcome;
    }
}
