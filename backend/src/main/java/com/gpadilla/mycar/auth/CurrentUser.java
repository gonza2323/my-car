package com.gpadilla.mycar.auth;

import com.gpadilla.mycar.enums.UserRole;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.security.Principal;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class CurrentUser implements Principal {

    private final Long id;
    private final List<UserRole> roles;

    @Override
    public String getName() {
        return id.toString();
    }
}
