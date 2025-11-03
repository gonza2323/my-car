package com.gpadilla.mycar.service.auth;

import com.gpadilla.mycar.config.AppProperties;
import com.gpadilla.mycar.dtos.auth.RefreshTokenDto;
import com.gpadilla.mycar.entity.RefreshToken;
import com.gpadilla.mycar.entity.Usuario;
import com.gpadilla.mycar.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private static final int TOKEN_LENGTH_BYTES = 32;
    private final RefreshTokenRepository repository;
    private final SecureRandom secureRandom = new SecureRandom();
    private final Base64.Encoder base64Encoder = Base64.getUrlEncoder().withoutPadding();
    private final AppProperties appProperties;

    private String generateRandomToken() {
        byte[] bytes = new byte[TOKEN_LENGTH_BYTES];
        secureRandom.nextBytes(bytes);
        return base64Encoder.encodeToString(bytes);
    }

    @Transactional
    public RefreshTokenDto createToken(Usuario user, boolean rememberMe) {
        AppProperties.Auth.RefreshToken tokenConfig = appProperties.auth().refreshToken();
        String token = generateRandomToken();
        Instant now = Instant.now();
        Instant expiry = rememberMe
                ? now.plus(tokenConfig.rememberMeDurationDays(), ChronoUnit.DAYS)
                : now.plus(tokenConfig.defaultDurationMinutes(), ChronoUnit.MINUTES);

        RefreshToken entity = RefreshToken.builder()
                .tokenHash(token)
                .user(user)
                .createdAt(now)
                .expiryDate(expiry)
                .rememberMe(rememberMe)
                .revoked(false)
                .build();

        repository.save(entity);

        return new RefreshTokenDto(token, expiry, rememberMe);
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> validateToken(String token) {
        return repository.findByTokenHashAndRevokedFalse(token)
                .filter(t -> t.getExpiryDate().isAfter(Instant.now()))
                .map(RefreshToken::getUser);
    }

    @Transactional
    public RefreshTokenDto rotateToken(String oldToken) {
        Optional<RefreshToken> existing = repository.findByTokenHashAndRevokedFalse(oldToken);
        existing.ifPresent(t -> {
            t.setRevoked(true);
            repository.save(t);
        });

        Usuario user = existing.map(RefreshToken::getUser)
                .orElseThrow(() -> new IllegalArgumentException("Invalid refresh token"));

        return createToken(user, existing.get().isRememberMe());
    }

    @Transactional
    public void revokeToken(String token) {
        repository.findByTokenHashAndRevokedFalse(token)
                .ifPresent(t -> {
                    t.setRevoked(true);
                    repository.save(t);
                });
    }
}
