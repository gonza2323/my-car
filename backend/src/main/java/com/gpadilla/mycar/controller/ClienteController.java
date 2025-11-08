package com.gpadilla.mycar.controller;

import com.gpadilla.mycar.dtos.cliente.ClienteCreateRequestDto;
import com.gpadilla.mycar.facade.ClienteFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class ClienteController {
    private final ClienteFacade clienteFacade;

    @PostMapping
    @PreAuthorize("hasRole('JEFE')")
    public ResponseEntity<Void> registrarCliente(@Valid ClienteCreateRequestDto request) {
        clienteFacade.registrarClientePorFormularioAdmin(request);
        return ResponseEntity.ok(null);
    }
}
