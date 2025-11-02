package com.gpadilla.mycar.component;

import com.gpadilla.mycar.config.AppProperties;
import com.gpadilla.mycar.dtos.auth.RefreshTokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
@RequiredArgsConstructor
public class RefreshTokenCookieHelper {
    private final AppProperties appProperties;

    public ResponseCookie createRefreshCookie(RefreshTokenDto token) {
        AppProperties.Auth.RefreshToken tokenConfig = appProperties.auth().refreshToken();

        ResponseCookie.ResponseCookieBuilder cookieBuilder = ResponseCookie.from(
                tokenConfig.cookie().name(),
                token.getToken()
        )
                .domain(appProperties.domain())
                .path("/")
                .httpOnly(true)
                .secure(true)
                .sameSite("None");

        if (token.isRememberMe()) {
            long maxAgeSeconds = Duration.between(Instant.now(), token.getExpiryDate()).getSeconds();
            cookieBuilder.maxAge(maxAgeSeconds);
        }

        return cookieBuilder.build();
    }

    public ResponseCookie clearCookie() {
        AppProperties.Auth.RefreshToken tokenConfig = appProperties.auth().refreshToken();

        return ResponseCookie.from(tokenConfig.cookie().name(), "")
                .domain(appProperties.domain())
                .path("/")
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .maxAge(0)
                .build();
    }
}
