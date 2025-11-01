package com.gpadilla.mycar.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final JwtService jwtService;

    // Simple in-memory map: token -> userId
    private final Map<String, Long> refreshTokens = new ConcurrentHashMap<>();

    // Generate a new refresh token
    public String generateRefreshToken(Long userId) {
        String token = UUID.randomUUID().toString();
        refreshTokens.put(token, userId);
        return token;
    }

    // Validate refresh token and return associated userId
    public Long validateRefreshToken(String token) {
        return refreshTokens.get(token); // returns null if invalid
    }

    // Rotate refresh token: remove old, add new
    public String rotateRefreshToken(String oldToken, Long userId) {
        refreshTokens.remove(oldToken);
        return generateRefreshToken(userId);
    }
}
