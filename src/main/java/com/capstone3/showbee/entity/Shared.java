package com.capstone3.showbee.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@Getter
@Table(name = "shared")
@AllArgsConstructor
@NoArgsConstructor
public class Shared {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User user; //참가자
    @ManyToOne
    private Schedule schedule; //공유된 일정

    public User getUserid() {
        return this.user;
    }
}

