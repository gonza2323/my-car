package com.gpadilla.mycar.auth;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "refresh-token.cookie")
public class RefreshTokenCookieProperties {
    private String name;
    private String path;
    private boolean httpOnly;
    private boolean secure;
    private String sameSite;
    private long maxAge;
}
