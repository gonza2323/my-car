package com.gpadilla.mycar.controller;

import com.gpadilla.mycar.dtos.promocion.PromocionCreateDto;
import com.gpadilla.mycar.dtos.promocion.PromocionViewDto;
import com.gpadilla.mycar.error.BusinessException;
import com.gpadilla.mycar.service.PromocionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/promociones")
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class PromocionController {

    private final PromocionService service;

    @GetMapping("/check")
    @PreAuthorize("hasAnyRole('JEFE', 'ADMINISTRATIVO')")
    public ResponseEntity<?> verificarDescuento(@Param("codigo") String codigo) {
        try {
            var promocion = service.validarCodigoPromocion(codigo);
            return ResponseEntity.ok(Map.of(
                    "valid", true,
                    "discount", promocion.getPorcentajeDescuento()
            ));
        } catch (BusinessException ex) {
            return ResponseEntity.ok(Map.of(
                    "valid", false,
                    "reason", ex.getMessage()
            ));
        }
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('JEFE', 'ADMINISTRATIVO')")
    public ResponseEntity<PromocionViewDto> create(@RequestBody PromocionCreateDto dto) {
        return ResponseEntity.ok(service.createAndReturnDto(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('JEFE', 'ADMINISTRATIVO')")
    public ResponseEntity<PromocionViewDto> update(@PathVariable Long id, @RequestBody PromocionCreateDto dto) {
        return ResponseEntity.ok(service.updateAndReturnDto(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('JEFE', 'ADMINISTRATIVO')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('JEFE', 'ADMINISTRATIVO')")
    public ResponseEntity<PromocionViewDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findDto(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('JEFE', 'ADMINISTRATIVO')")
    public ResponseEntity<Page<PromocionViewDto>> findAll(Pageable pageable) {
        return ResponseEntity.ok(service.findDtos(pageable));
    }
}
