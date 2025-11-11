package com.gpadilla.mycar.controller;

import com.gpadilla.mycar.dtos.caracteristicasAuto.*;
import com.gpadilla.mycar.service.CaracteristicasAutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/modelos")
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class CaracteristicasAutoController {

    private final CaracteristicasAutoService service;

    // 🔹 Crear modelo
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRATIVO', 'JEFE')")
    public ResponseEntity<CaracteristicasAutoDetailDto> create(@RequestBody CaracteristicasAutoCreateDto dto) {
        return ResponseEntity.ok(service.createAndReturnDto(dto));
    }

    // 🔹 Actualizar modelo
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATIVO', 'JEFE')")
    public ResponseEntity<CaracteristicasAutoDetailDto> update(@PathVariable Long id, @RequestBody CaracteristicasAutoUpdateDto dto) {
        return ResponseEntity.ok(service.updateAndReturnDto(id, dto));
    }

    // 🔹 Eliminar modelo
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATIVO', 'JEFE')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // 🔹 Obtener modelo por ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATIVO', 'JEFE')")
    public ResponseEntity<CaracteristicasAutoDetailDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findDto(id));
    }

    // 🔹 Listar modelos con paginación
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRATIVO', 'JEFE')")
    public ResponseEntity<Page<CaracteristicasAutoSummaryDto>> findAll(Pageable pageable) {
        return ResponseEntity.ok(service.findDtos(pageable));
    }

    @GetMapping("/available")
    public ResponseEntity<Page<CaracteristicasAutoDisponible>> buscarModelosDisponiblesPorFecha(
            Pageable pageable,
            @Param("fechaDesde") LocalDate fechaDesde,
            @Param("fechaHasta") LocalDate fechaHasta) {

        var modelosDisponibles = service.encontrarModelosDisponiblesParaAlquiler(pageable, fechaDesde, fechaHasta);
        return ResponseEntity.ok(modelosDisponibles);
    }

    @GetMapping("/{modeloId}/available")
    public ResponseEntity<CaracteristicasAutoDisponible> buscarModelosDisponiblesPorFecha(
            @PathVariable("modeloId") Long modeloId,
            @Param("fechaDesde") LocalDate fechaDesde,
            @Param("fechaHasta") LocalDate fechaHasta) {

        var detallesModeloDisponible = service.consultarModeloDisponible(modeloId, fechaDesde, fechaHasta);
        return ResponseEntity.ok(detallesModeloDisponible);
    }
}
