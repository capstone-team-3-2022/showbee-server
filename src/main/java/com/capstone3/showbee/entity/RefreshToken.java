package com.capstone3.showbee.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.StringTokenizer;

@Entity
@Table(name = "refresh_token")
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class RefreshToken {
    @Id
    private String rId;
    @Column(nullable = false)
    private Long key;
    @Column(nullable = false)
    private String token;

    public RefreshToken updateToken(String token){
        this.token = token;
        return this;
    }
}
