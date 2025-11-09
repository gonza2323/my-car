package com.gpadilla.mycar.controller.geo;

import com.gpadilla.mycar.dtos.geo.pais.PaisCreateOrUpdateDto;
import com.gpadilla.mycar.dtos.geo.pais.PaisViewDto;
import com.gpadilla.mycar.service.geo.PaisService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/paises")
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class PaisController {

    private final PaisService service;

    @PostMapping
    @PreAuthorize("hasRole('JEFE')")
    public ResponseEntity<PaisViewDto> create(@RequestBody PaisCreateOrUpdateDto dto) {
        return ResponseEntity.ok(service.createAndReturnDto(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('JEFE')")
    public ResponseEntity<PaisViewDto> update(@PathVariable Long id, @RequestBody PaisCreateOrUpdateDto dto) {
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
    public ResponseEntity<PaisViewDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findDto(id));
    }

    @GetMapping
    public ResponseEntity<Page<PaisViewDto>> findAll(Pageable pageable) {
        return ResponseEntity.ok(service.findDtos(pageable));
    }
}
