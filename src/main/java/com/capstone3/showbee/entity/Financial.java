package com.capstone3.showbee.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Builder
@Getter
@Table(name="financial")
@AllArgsConstructor
@NoArgsConstructor
public class Financial {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fid;

    private Date date;
    private int price;
    private String content;
    private String category;

}
