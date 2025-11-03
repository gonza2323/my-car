package com.gpadilla.mycar.dtos.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gpadilla.mycar.enums.UserRole;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collection;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthStatusDto {
    boolean isAuthenticated;
    Long userId;
    Collection<UserRole> roles;

    public static AuthStatusDto authenticated(Long userId, Collection<UserRole> roles) {
        return new AuthStatusDto(true, userId, roles);
    }

    public static AuthStatusDto unauthenticated() {
        return new AuthStatusDto(false, null, null);
    }
}
