package com.gpadilla.mycar.auth;

import com.gpadilla.mycar.enums.UserRole;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public interface CurrentUser {
    Long getId();
    Collection<UserRole> getRoles();
}
