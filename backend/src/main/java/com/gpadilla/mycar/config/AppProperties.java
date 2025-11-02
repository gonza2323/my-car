package com.gpadilla.mycar.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record AppProperties(
    Auth auth,
    String baseUrl,
    String frontendUrl,
    String domain
) {
    public record Auth(RefreshToken refreshToken, AccessToken accessToken) {
        public record RefreshToken(Cookie cookie,
            long days
        ) {
            public record Cookie(
                String name,
                String path,
                boolean httpOnly,
                boolean secure,
                String sameSite
            ) {
            }
        }

        public record AccessToken(
            String secret,
            long minutes
        ) {
        }
    }
}