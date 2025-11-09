package com.gpadilla.mycar.controller.geo;

import com.gpadilla.mycar.dtos.geo.departamento.DepartamentoCreateOrUpdateDto;
import com.gpadilla.mycar.dtos.geo.departamento.DepartamentoViewDto;
import com.gpadilla.mycar.service.geo.DepartamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/departamentos")
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class DepartamentoController {

    private final DepartamentoService service;

    @PostMapping
    @PreAuthorize("hasRole('JEFE')")
    public ResponseEntity<DepartamentoViewDto> create(@RequestBody DepartamentoCreateOrUpdateDto dto) {
        return ResponseEntity.ok(service.createAndReturnDto(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('JEFE')")
    public ResponseEntity<DepartamentoViewDto> update(@PathVariable Long id, @RequestBody DepartamentoCreateOrUpdateDto dto) {
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
    public ResponseEntity<DepartamentoViewDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findDto(id));
    }

    @GetMapping
    public ResponseEntity<Page<DepartamentoViewDto>> findAll(Pageable pageable, @Param("provinciaId") Long provinciaId) {
        return ResponseEntity.ok(service.findDtos(pageable, provinciaId));
    }
}
