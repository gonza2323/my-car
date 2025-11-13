package com.gpadilla.mycar.controller;

import com.gpadilla.mycar.dtos.costoAuto.CostoAutoCreateDto;
import com.gpadilla.mycar.dtos.costoAuto.CostoAutoDto;
import com.gpadilla.mycar.dtos.costoAuto.CostoAutoUpdateDto;
import com.gpadilla.mycar.service.CostoAutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/costos")
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class CostoAutoController {

    private final CostoAutoService service;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRATIVO', 'JEFE')")
    public ResponseEntity<CostoAutoDto> create(@RequestBody CostoAutoCreateDto dto) {
        return ResponseEntity.ok(service.createAndReturnDto(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATIVO', 'JEFE')")
    public ResponseEntity<CostoAutoDto> update(@PathVariable Long id, @RequestBody CostoAutoUpdateDto dto) {
        return ResponseEntity.ok(service.updateAndReturnDto(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATIVO', 'JEFE')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATIVO', 'JEFE')")
    public ResponseEntity<CostoAutoDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findDto(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRATIVO', 'JEFE')")
    public ResponseEntity<Page<CostoAutoDto>> listarCostosDeModelo(Pageable pageable, @Param("modeloId") Long modeloId) {
        return ResponseEntity.ok(service.findDtosDeModelo(pageable, modeloId));
    }
}
