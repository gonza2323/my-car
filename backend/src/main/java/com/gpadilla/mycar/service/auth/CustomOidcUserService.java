package com.gpadilla.mycar.service.auth;

import com.gpadilla.mycar.auth.CustomOidcUser;
import com.gpadilla.mycar.entity.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomOidcUserService extends OidcUserService {

    private final AuthService authService;

    @Override
    @Transactional
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);

        String providerId = oidcUser.getAttribute("sub");
        String email = oidcUser.getAttribute("email");
        boolean emailVerified = Boolean.TRUE.equals(oidcUser.getAttribute("email_verified"));

        Usuario usuario = authService.handleOidcLogin(providerId, email, emailVerified);

        return new CustomOidcUser(usuario.getId(), oidcUser);
    }
}
