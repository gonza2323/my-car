package com.gpadilla.mycar.auth;

import com.gpadilla.mycar.config.AppProperties;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OidcFailureHandler implements AuthenticationFailureHandler {
    private final AppProperties appProperties;

    public OidcFailureHandler(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception)
            throws IOException {
        response.sendRedirect(appProperties.frontendUrl() + "/login?error");
    }
}
