package com.gpadilla.mycar.controller;

import com.gpadilla.mycar.dtos.empresa.EmpresaCreateOrUpdateDto;
import com.gpadilla.mycar.dtos.empresa.EmpresaDetailDto;
import com.gpadilla.mycar.service.EmpresaService;
import jakarta.validation.Valid;
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

    @GetMapping("/detalle")
    public ResponseEntity<EmpresaDetailDto> obtenerDetalle() {
        return ResponseEntity.ok(service.findSingletonDto());
    }


    @PutMapping("/contacto")
    @PreAuthorize("hasAnyRole('ADMINISTRATIVO','JEFE')")
    public ResponseEntity<EmpresaDetailDto> actualizarContacto(@Valid @RequestBody EmpresaCreateOrUpdateDto dto) {
        return ResponseEntity.ok(service.updateContacto(dto));
    }
}
