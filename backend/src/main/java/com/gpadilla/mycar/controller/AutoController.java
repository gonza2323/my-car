package com.gpadilla.mycar.controller;

import com.gpadilla.mycar.dtos.auto.AutoCreateDto;
import com.gpadilla.mycar.dtos.auto.AutoDetailDto;
import com.gpadilla.mycar.dtos.auto.AutoSummaryDto;
import com.gpadilla.mycar.dtos.auto.AutoUpdateDto;
import com.gpadilla.mycar.service.AutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/autos")
@RequiredArgsConstructor
public class AutoController {

    private final AutoService service;

    // 🔹 Crear un vehículo
    @PostMapping
    public ResponseEntity<AutoDetailDto> create(@RequestBody AutoCreateDto dto) {
        return ResponseEntity.ok(service.createAndReturnDto(dto));
    }

    // 🔹 Actualizar un vehículo
    @PutMapping("/{id}")
    public ResponseEntity<AutoDetailDto> update(@PathVariable Long id, @RequestBody AutoUpdateDto dto) {
        return ResponseEntity.ok(service.updateAndReturnDto(id, dto));
    }

    // 🔹 Eliminar un vehículo (soft delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // 🔹 Obtener un vehículo por ID
    @GetMapping("/{id}")
    public ResponseEntity<AutoDetailDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findDto(id));
    }

    // 🔹 Listar vehículos con paginación
    @GetMapping
    public ResponseEntity<Page<AutoSummaryDto>> findAll(Pageable pageable) {
        return ResponseEntity.ok(service.findDtos(pageable));
    }
}

