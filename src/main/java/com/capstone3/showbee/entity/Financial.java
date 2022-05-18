package com.capstone3.showbee.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Table(name="financial")
@NoArgsConstructor
public class Financial {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fid;
    private Date date;
    private int price;
    private String content;
    private String category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;

    @Builder
    public Financial(Date date, int price, String content, String category, User user, Long fid){
        this.category = category;
        this.date = date;
        this.content = content;
        this.user = user;
        this.price = price;
        this.fid = fid;
    }
}
