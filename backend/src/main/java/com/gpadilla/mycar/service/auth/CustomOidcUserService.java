package com.gpadilla.mycar.service.auth;

import com.gpadilla.mycar.auth.CustomUserDetails;
import com.gpadilla.mycar.entity.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomOidcUserService implements OAuth2UserService<OidcUserRequest, OidcUser> {

    private final AuthService authService;

    @Override
    @Transactional
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = new OidcUserService().loadUser(userRequest);

        String providerId = oidcUser.getAttribute("sub");
        String email = oidcUser.getAttribute("email");
        boolean emailVerified = Boolean.TRUE.equals(oidcUser.getAttribute("email_verified"));

        Usuario usuario = authService.loginOrSignupWithProvider(providerId, email, emailVerified);

        return new CustomUserDetails(usuario.getId(), usuario.getEmail(), usuario.getPassword(), List.of(usuario.getRol()), oidcUser);
    }
}
