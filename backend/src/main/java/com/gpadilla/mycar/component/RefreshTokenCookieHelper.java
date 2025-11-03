package com.gpadilla.mycar.component;

import com.gpadilla.mycar.config.AppProperties;
import com.gpadilla.mycar.dtos.auth.RefreshTokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.time.Instant;

@Component
@RequiredArgsConstructor
public class RefreshTokenCookieHelper {
    private final AppProperties appProperties;
    private final RestClient.Builder builder;

    public ResponseCookie createRefreshCookie(RefreshTokenDto token) {
        AppProperties.Auth.RefreshToken tokenConfig = appProperties.auth().refreshToken();

        var builder = getCookieBuilder()
                .value(token.getToken());

        if (token.isRememberMe()) {
            long maxAgeSeconds = Duration.between(Instant.now(), token.getExpiryDate()).getSeconds();
            builder.maxAge(maxAgeSeconds);
        }

        return builder.build();
    }

    public ResponseCookie clearCookie() {
        var builder = getCookieBuilder();
        return builder
                .value("")
                .maxAge(0)
                .build();
    }

    private ResponseCookie.ResponseCookieBuilder getCookieBuilder() {
        AppProperties.Auth.RefreshToken tokenConfig = appProperties.auth().refreshToken();

        var builder = ResponseCookie.from(
                tokenConfig.cookie().name()
                )
                .path("/")
                .httpOnly(true)
                .sameSite(tokenConfig.cookie().sameSite())
                .secure(tokenConfig.cookie().secure());

        if (!tokenConfig.cookie().domain().equals("localhost"))
            builder.domain(tokenConfig.cookie().domain());

        return builder;
    }
}
