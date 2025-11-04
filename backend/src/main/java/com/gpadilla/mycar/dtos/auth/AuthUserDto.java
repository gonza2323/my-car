package com.gpadilla.mycar.dtos.auth;

import com.gpadilla.mycar.enums.UserRole;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Collection;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AuthUserDto {
    Long userId;
    Collection<UserRole> roles;
}
