package com.gpadilla.mycar.controller;

import com.gpadilla.mycar.auth.CurrentUser;
import com.gpadilla.mycar.component.RefreshTokenCookieHelper;
import com.gpadilla.mycar.dtos.auth.LoginRequestDto;
import com.gpadilla.mycar.dtos.auth.RefreshTokenDto;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@EnableMethodSecurity(prePostEnabled = true)
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AccessTokenService accessTokenService;
    private final JwtEncoder jwtEncoder;
    private final UsuarioService usuarioService;
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenCookieHelper refreshTokenCookieHelper;

    @PostMapping("/login")
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequestDto loginRequest) {
        CurrentUser user = authService.getAuthenticatedUser(loginRequest);

        Usuario usuario = usuarioService.find(user.getId());
        String accessToken = accessTokenService.createToken(user.getId(), user.getRoles());
        RefreshTokenDto refreshToken = refreshTokenService.createToken(usuario, loginRequest.isRememberMe());
        ResponseCookie refreshTokenCookie = refreshTokenCookieHelper.createRefreshCookie(refreshToken);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(new LoginResponse(accessToken));
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
    public ResponseEntity<Map<String, Object>> authStatus(@AuthenticationPrincipal CurrentUser user) {
        if (user == null) {
            return ResponseEntity.ok(Map.of(
                    "authenticated", false
            ));
        }

        return ResponseEntity.ok(Map.of(
                "authenticated", true,
                "id", user.getId(),
                "roles", user.getRoles()
        ));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@CookieValue(name = "refreshToken", required = false) String refreshToken) {
        if (refreshToken != null) {
            Optional<Usuario> opt = refreshTokenService.validateToken(refreshToken);

            if (opt.isPresent()) {
                Usuario usuario = opt.get();
                String newAccessToken = accessTokenService.createToken(usuario.getId(), List.of(usuario.getRol()));
                RefreshTokenDto newRefreshToken = refreshTokenService.rotateToken(refreshToken);
                ResponseCookie refreshTokenCookie = refreshTokenCookieHelper.createRefreshCookie(newRefreshToken);

                return ResponseEntity.ok()
                        .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                        .body(new LoginResponse(newAccessToken));
            }
        }

        ResponseCookie cookie = refreshTokenCookieHelper.clearCookie();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(Map.of("error", "Invalid refresh token"));
    }

    public record LoginResponse(String token) {}
}
