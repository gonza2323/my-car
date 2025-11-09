package com.gpadilla.mycar.controller.geo;

import com.gpadilla.mycar.dtos.geo.localidad.LocalidadCreateOrUpdateDto;
import com.gpadilla.mycar.dtos.geo.localidad.LocalidadViewDto;
import com.gpadilla.mycar.service.geo.LocalidadService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/localidades")
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class LocalidadController {

    private final LocalidadService service;

    @PostMapping
    @PreAuthorize("hasRole('JEFE')")
    public ResponseEntity<LocalidadViewDto> create(@RequestBody LocalidadCreateOrUpdateDto dto) {
        return ResponseEntity.ok(service.createAndReturnDto(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('JEFE')")
    public ResponseEntity<LocalidadViewDto> update(@PathVariable Long id, @RequestBody LocalidadCreateOrUpdateDto dto) {
        return ResponseEntity.ok(service.updateAndReturnDto(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('JEFE')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('JEFE', 'ADMINISTRATIVO')")
    public ResponseEntity<LocalidadViewDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findDto(id));
    }

    @GetMapping
    public ResponseEntity<Page<LocalidadViewDto>> findAll(Pageable pageable, @Param("departamentoId") Long departamentoId) {
        return ResponseEntity.ok(service.findDtos(pageable, departamentoId));
    }
}
