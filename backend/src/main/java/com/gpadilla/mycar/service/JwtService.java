package com.gpadilla.mycar.service;

import com.gpadilla.mycar.auth.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtEncoder encoder;

    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        long expiry = 60 * 60; // 1 hora

        List<String> roles = authentication.getAuthorities().stream()
                .filter(auth -> auth.getAuthority().startsWith("ROLE_"))
                .map(auth -> auth.getAuthority().substring(5))
                .toList();

        Long userId;
        if (authentication.getPrincipal() instanceof CustomUserDetails customUserDetails) {
            userId = customUserDetails.getId();
        } else {
            throw new IllegalArgumentException();
        }

        JwsHeader jwsHeader = JwsHeader.with(() -> "HS256").build();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .subject(userId.toString())
                .claim("roles", roles)
                .build();

        return this.encoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }
}
