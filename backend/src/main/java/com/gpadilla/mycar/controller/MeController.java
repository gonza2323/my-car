package com.gpadilla.mycar.controller;

import com.gpadilla.mycar.auth.CurrentUser;
import com.gpadilla.mycar.dtos.cliente.ClienteCompleteProfileDto;
import com.gpadilla.mycar.dtos.usuario.UsuarioDetailDto;
import com.gpadilla.mycar.facade.ClienteFacade;
import com.gpadilla.mycar.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/me")
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class MeController {

    private final UsuarioService usuarioService;
    private final ClienteFacade clienteFacade;

    @GetMapping("/account")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UsuarioDetailDto> getCurrentUser(@AuthenticationPrincipal CurrentUser user) {
        UsuarioDetailDto usuario = usuarioService.findDto(user.getId());
        return ResponseEntity.ok(usuario);
    }

    @PostMapping("/complete-profile")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<Void> completarPerfilCliente(@AuthenticationPrincipal CurrentUser user, ClienteCompleteProfileDto dto) {
        clienteFacade.completarPerfilDeCliente(user.getId(), dto);
        return ResponseEntity.ok(null);
    }
}
