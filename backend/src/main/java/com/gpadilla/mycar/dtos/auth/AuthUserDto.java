package com.gpadilla.mycar.dtos.auth;

import com.gpadilla.mycar.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
    private Boolean hasCompletedProfile;
    private Boolean mustChangePassword;
}
