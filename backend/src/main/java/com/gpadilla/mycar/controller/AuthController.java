package com.gpadilla.mycar.controller;

import com.gpadilla.mycar.auth.CustomUserDetails;
import com.gpadilla.mycar.dtos.auth.LoginRequest;
import com.gpadilla.mycar.dtos.usuario.UsuarioDetailDto;
import com.gpadilla.mycar.service.AuthService;
import com.gpadilla.mycar.service.JwtService;
import com.gpadilla.mycar.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@EnableMethodSecurity(prePostEnabled = true)
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final JwtEncoder jwtEncoder;
    private final UsuarioService usuarioService;
    private final AuthService authService;

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UsuarioDetailDto> getCurrentUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        UsuarioDetailDto usuario = usuarioService.findDto(userDetails.getId());
        return ResponseEntity.ok(usuario);
    }

    @PostMapping("/login")
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        String token = authService.authenticateAndGetToken(loginRequest);
        return ResponseEntity.ok(new LoginResponse(token));
    }

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> authStatus(@AuthenticationPrincipal CustomUserDetails user) {
        if (user == null) {
            return ResponseEntity.ok(Map.of(
                    "authenticated", false
            ));
        }

        return ResponseEntity.ok(Map.of(
                "authenticated", true,
                "userId", user.getId(),
                "roles", user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList()
        ));
    }

    public record LoginResponse(String token) {}
}
