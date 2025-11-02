package com.gpadilla.mycar.auth;

import com.gpadilla.mycar.enums.UserRole;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Builder
public class CustomUserDetails implements UserDetails, OidcUser, CurrentUser {

    @Getter
    private final Long id;
    private final String email;
    private final String password;
    @Getter
    private final Collection<UserRole> roles;
    private final OidcUser oidcUser;

    public CustomUserDetails(Long id, String email, String password, Collection<UserRole> roles) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.oidcUser = null;
    }

    public CustomUserDetails(Long id, String email, String password, Collection<UserRole> roles, OidcUser oidcUser) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.oidcUser = oidcUser;
    }


    // Métodos de UserDetails

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + roles));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    // Métodos de OidcUserInfo

    @Override
    public Map<String, Object> getClaims() {
        return oidcUser != null ? oidcUser.getClaims() : Collections.emptyMap();
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return oidcUser != null ? oidcUser.getUserInfo() : null;
    }

    @Override
    public OidcIdToken getIdToken() {
        return oidcUser != null ? oidcUser.getIdToken() : null;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oidcUser != null ? oidcUser.getAttributes() : Collections.emptyMap();
    }

    @Override
    public String getName() {
        return oidcUser != null ? oidcUser.getName() : null;
    }
}