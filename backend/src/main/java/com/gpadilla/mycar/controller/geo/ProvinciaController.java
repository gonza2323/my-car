package com.gpadilla.mycar.controller.geo;

import com.gpadilla.mycar.dtos.geo.provincia.ProvinciaCreateOrUpdateDto;
import com.gpadilla.mycar.dtos.geo.provincia.ProvinciaViewDto;
import com.gpadilla.mycar.service.geo.ProvinciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/provincias")
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class ProvinciaController {

    private final ProvinciaService service;

    @PostMapping
    @PreAuthorize("hasRole('JEFE')")
    public ResponseEntity<ProvinciaViewDto> create(@RequestBody ProvinciaCreateOrUpdateDto dto) {
        return ResponseEntity.ok(service.createAndReturnDto(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('JEFE')")
    public ResponseEntity<ProvinciaViewDto> update(@PathVariable Long id, @RequestBody ProvinciaCreateOrUpdateDto dto) {
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
    public ResponseEntity<ProvinciaViewDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findDto(id));
    }

    @GetMapping
    public ResponseEntity<Page<ProvinciaViewDto>> findAll(Pageable pageable, @Param("paisId") Long paisId) {
        return ResponseEntity.ok(service.findDtos(pageable, paisId));
    }
}
