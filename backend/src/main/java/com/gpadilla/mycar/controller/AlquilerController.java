package com.gpadilla.mycar.controller;

import com.gpadilla.mycar.dtos.alquiler.AlquilerCreateRequestDto;
import com.gpadilla.mycar.dtos.alquiler.AlquilerSummaryDto;
import com.gpadilla.mycar.facade.AlquilerFacade;
import com.gpadilla.mycar.service.AlquilerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/alquileres")
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class AlquilerController {

    private final AlquilerFacade alquilerFacade;
    private final AlquilerService alquilerService;

    @GetMapping
    @PreAuthorize("hasAnyRole('JEFE', 'ADMINISTRATIVO')")
    public ResponseEntity<Page<AlquilerSummaryDto>> find(Pageable pageable) {
        Page<AlquilerSummaryDto> alquileres = alquilerService.findDtos(pageable);
        return ResponseEntity.ok(alquileres);
    }

    @PostMapping()
    @PreAuthorize("hasAnyRole('JEFE', 'ADMINISTRATIVO')")
    public ResponseEntity<?> registrarAlquiler(@Valid @RequestBody AlquilerCreateRequestDto dto) {
        // TODO: FALTA DOCUMENTACION
        alquilerFacade.registrarAlquiler(dto);
        return ResponseEntity.noContent().build();
    }
}
