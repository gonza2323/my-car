package com.gpadilla.mycar.dtos.auth;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenDto {
    private String token;
    private Instant expiryDate;
    private boolean rememberMe;
}
