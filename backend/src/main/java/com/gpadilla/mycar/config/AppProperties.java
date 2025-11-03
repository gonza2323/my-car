package com.gpadilla.mycar.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "app")
public record AppProperties(
    Auth auth,
    String baseUrl,
    String frontendUrl,
    List<String> corsOrigins
) {
    public record Auth(RefreshToken refreshToken, AccessToken accessToken) {

        public record RefreshToken(Cookie cookie,
            long defaultDurationMinutes,
            long rememberMeDurationDays
        ) {
            public record Cookie(
                String name,
                String domain,
                String sameSite,
                boolean secure
            ) {
            }
        }

        public record AccessToken(
            String secret,
            long durationMinutes
        ) {
        }
    }
}