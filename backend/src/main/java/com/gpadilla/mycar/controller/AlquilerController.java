package com.gpadilla.mycar.controller;

import com.gpadilla.mycar.dtos.alquiler.AlquilerCreateDto;
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

    @PostMapping()
    @PreAuthorize("hasAnyRole('JEFE', 'ADMINISTRATIVO')")
    public ResponseEntity<?> registrarAlquiler(@Valid @RequestBody AlquilerCreateDto dto) {
        // TODO: REGISTRAR ALQUILER
        // TODO: FALTA DOCUMENTACION
        // alquilerService.registrarAlquiler(dto, documentoDni, documentoLicencia);
        return ResponseEntity.noContent().build();
    }
}
