package com.gpadilla.mycar.controller.geo;

import com.gpadilla.mycar.dtos.geo.nacionalidad.NacionalidadCreateOrUpdateDto;
import com.gpadilla.mycar.dtos.geo.nacionalidad.NacionalidadViewDto;
import com.gpadilla.mycar.service.geo.NacionalidadService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/nacionalidades")
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class NacionalidadController {

    private final NacionalidadService service;

    @PostMapping
    @PreAuthorize("hasRole('JEFE')")
    public ResponseEntity<NacionalidadViewDto> create(@RequestBody NacionalidadCreateOrUpdateDto dto) {
        return ResponseEntity.ok(service.createAndReturnDto(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('JEFE')")
    public ResponseEntity<NacionalidadViewDto> update(@PathVariable Long id, @RequestBody NacionalidadCreateOrUpdateDto dto) {
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
    public ResponseEntity<NacionalidadViewDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findDto(id));
    }

    @GetMapping
    public ResponseEntity<Page<NacionalidadViewDto>> findAll(Pageable pageable) {
        return ResponseEntity.ok(service.findDtos(pageable));
    }
}
