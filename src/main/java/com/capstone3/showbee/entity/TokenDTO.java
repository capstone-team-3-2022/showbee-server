package com.capstone3.showbee.entity;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenDTO {
    private String accessToken;
    private String refreshToken;
    private String grantType;
    private Long accessTokenExpired;
}
