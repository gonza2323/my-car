package com.gpadilla.mycar.controller;

import com.gpadilla.mycar.dtos.alquiler.AlquilerCreateRequestDto;
import com.gpadilla.mycar.facade.AlquilerFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/alquileres")
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class AlquilerController {

    private final AlquilerFacade alquilerFacade;

    @PostMapping()
    @PreAuthorize("hasAnyRole('JEFE', 'ADMINISTRATIVO')")
    public ResponseEntity<?> registrarAlquiler(@Valid @RequestBody AlquilerCreateRequestDto dto) {
        // TODO: FALTA DOCUMENTACION
        alquilerFacade.registrarAlquiler(dto);
        return ResponseEntity.noContent().build();
    }
}
