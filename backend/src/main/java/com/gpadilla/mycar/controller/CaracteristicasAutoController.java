package com.gpadilla.mycar.controller;

import com.gpadilla.mycar.dtos.caracteristicasAuto.CaracteristicasAutoCreateDto;
import com.gpadilla.mycar.dtos.caracteristicasAuto.CaracteristicasAutoDetailDto;
import com.gpadilla.mycar.dtos.caracteristicasAuto.CaracteristicasAutoSummaryDto;
import com.gpadilla.mycar.dtos.caracteristicasAuto.CaracteristicasAutoUpdateDto;
import com.gpadilla.mycar.service.CaracteristicasAutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/modelos")
@RequiredArgsConstructor
public class CaracteristicasAutoController {

    private final CaracteristicasAutoService service;

    // 🔹 Crear modelo
    @PostMapping
    public ResponseEntity<CaracteristicasAutoDetailDto> create(@RequestBody CaracteristicasAutoCreateDto dto) {
        return ResponseEntity.ok(service.createAndReturnDto(dto));
    }

    // 🔹 Actualizar modelo
    @PutMapping("/{id}")
    public ResponseEntity<CaracteristicasAutoDetailDto> update(@PathVariable Long id, @RequestBody CaracteristicasAutoUpdateDto dto) {
        dto.setId(id);
        return ResponseEntity.ok(service.updateAndReturnDto(dto));
    }

    // 🔹 Eliminar modelo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // 🔹 Obtener modelo por ID
    @GetMapping("/{id}")
    public ResponseEntity<CaracteristicasAutoDetailDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findDto(id));
    }

    // 🔹 Listar modelos con paginación
    @GetMapping
    public ResponseEntity<Page<CaracteristicasAutoSummaryDto>> findAll(Pageable pageable) {
        return ResponseEntity.ok(service.findDtos(pageable));
    }
}
