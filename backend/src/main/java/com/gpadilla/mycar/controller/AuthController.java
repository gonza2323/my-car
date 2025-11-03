package com.gpadilla.mycar.controller;

import com.gpadilla.mycar.auth.CurrentUser;
import com.gpadilla.mycar.component.RefreshTokenCookieHelper;
import com.gpadilla.mycar.dtos.auth.*;
import com.gpadilla.mycar.entity.Usuario;
import com.gpadilla.mycar.service.UsuarioService;
import com.gpadilla.mycar.service.auth.AccessTokenService;
import com.gpadilla.mycar.service.auth.AuthService;
import com.gpadilla.mycar.service.auth.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@EnableMethodSecurity(prePostEnabled = true)
public class AuthController {

    private final AccessTokenService accessTokenService;
    private final UsuarioService usuarioService;
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenCookieHelper refreshTokenCookieHelper;

    @PostMapping("/login")
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto loginRequest) {
        CurrentUser user = authService.getAuthenticatedUser(loginRequest);

        Usuario usuario = usuarioService.find(user.getId());
        AccessTokenDto accessToken = accessTokenService.createToken(user.getId(), user.getRoles());
        RefreshTokenDto refreshToken = refreshTokenService.createToken(usuario, loginRequest.isRemember());
        ResponseCookie refreshTokenCookie = refreshTokenCookieHelper.createRefreshCookie(refreshToken);

        AuthResponseDto response = AuthResponseDto.builder()
                .token(accessToken)
                .status(AuthStatusDto.authenticated(user.getId(), user.getRoles()))
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@CookieValue(name = "refreshToken", required = false) String refreshToken) {
        if (refreshToken == null) {
            return ResponseEntity.ok().build();
        }

        refreshTokenService.revokeToken(refreshToken);
        ResponseCookie cookie = refreshTokenCookieHelper.clearCookie();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @GetMapping("/status")
    public ResponseEntity<AuthStatusDto> authStatus(@AuthenticationPrincipal CurrentUser user) {
        if (user != null) {
            return ResponseEntity.ok(AuthStatusDto.authenticated(user.getId(), user.getRoles()));
        }
        return ResponseEntity.ok(AuthStatusDto.unauthenticated());
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@CookieValue(name = "refreshToken", required = false) String refreshToken) {
        if (refreshToken != null) {
            Optional<Usuario> opt = refreshTokenService.validateToken(refreshToken);

            if (opt.isPresent()) {
                Usuario usuario = opt.get();
                AccessTokenDto newAccessToken = accessTokenService.createToken(usuario.getId(), List.of(usuario.getRol()));
                RefreshTokenDto newRefreshToken = refreshTokenService.rotateToken(refreshToken);
                ResponseCookie refreshTokenCookie = refreshTokenCookieHelper.createRefreshCookie(newRefreshToken);

                return ResponseEntity.ok()
                        .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                        .body(newAccessToken);
            }
        }

        ResponseCookie cookie = refreshTokenCookieHelper.clearCookie();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(Map.of("error", "Invalid refresh token"));
    }
}
