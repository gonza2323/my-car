package com.gpadilla.mycar.controller;

import com.gpadilla.mycar.auth.CustomUserDetails;
import com.gpadilla.mycar.dtos.usuario.UsuarioDetailDto;
import com.gpadilla.mycar.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class MeController {

    private final UsuarioService usuarioService;

    @GetMapping("/account")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UsuarioDetailDto> getCurrentUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        UsuarioDetailDto usuario = usuarioService.findDto(userDetails.getId());
        return ResponseEntity.ok(usuario);
    }
}
