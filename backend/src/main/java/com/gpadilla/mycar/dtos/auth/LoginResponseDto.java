package com.gpadilla.mycar.dtos.auth;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {
    AccessTokenDto token;
    AuthUserDto user;
}
