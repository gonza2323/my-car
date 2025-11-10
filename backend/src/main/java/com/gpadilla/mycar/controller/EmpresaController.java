package com.gpadilla.mycar.controller;

import com.gpadilla.mycar.dtos.empresa.EmpresaCreateOrUpdateDto;
import com.gpadilla.mycar.dtos.empresa.EmpresaDetailDto;
import com.gpadilla.mycar.service.EmpresaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/empresa")
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class EmpresaController {

    private final EmpresaService service;

    // Inicializa la única empresa si no existe (idempotente)
    @PostMapping("/init")
    @PreAuthorize("hasAnyRole('ADMINISTRATIVO','JEFE')")
    public ResponseEntity<EmpresaDetailDto> init(@RequestBody EmpresaCreateOrUpdateDto dto) {
        return ResponseEntity.ok(service.createIfAbsentAndReturnDto(dto));
    }

    // Devuelve el detalle de la única empresa
    @GetMapping
    public ResponseEntity<EmpresaDetailDto> get() {
        return ResponseEntity.ok(service.findSingletonDto());
    }

    // Actualiza nombre y/o direccionId
    @PutMapping
    @PreAuthorize("hasAnyRole('ADMINISTRATIVO','JEFE')")
    public ResponseEntity<EmpresaDetailDto> update(@RequestBody EmpresaCreateOrUpdateDto dto) {
        return ResponseEntity.ok(service.updateSingletonAndReturnDto(dto));
    }
}
