package com.capstone3.showbee.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class TokenRequestDTO {
    String accessToken;
    String refreshToken;
}
