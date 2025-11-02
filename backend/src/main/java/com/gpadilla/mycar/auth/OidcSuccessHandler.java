package com.gpadilla.mycar.auth;

import com.gpadilla.mycar.component.RefreshTokenCookieHelper;
import com.gpadilla.mycar.config.AppProperties;
import com.gpadilla.mycar.dtos.auth.RefreshTokenDto;
import com.gpadilla.mycar.entity.Usuario;
import com.gpadilla.mycar.service.auth.RefreshTokenService;
import com.gpadilla.mycar.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OidcSuccessHandler implements AuthenticationSuccessHandler {

    private final RefreshTokenCookieHelper refreshTokenCookieHelper;
    private final RefreshTokenService refreshTokenService;
    private final AppProperties appProperties;
    private final UsuarioService usuarioService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        CurrentUser userDetails = (CurrentUser) authentication.getPrincipal();
        Usuario user = usuarioService.find(userDetails.getId());

        RefreshTokenDto refreshToken = refreshTokenService.createToken(user, false);
        ResponseCookie cookie = refreshTokenCookieHelper.createRefreshCookie(refreshToken);

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        response.sendRedirect(appProperties.frontendUrl());
    }
}