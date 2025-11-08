package com.gpadilla.mycar.controller;

import com.gpadilla.mycar.dtos.costoAuto.CostoAutoCreateDto;
import com.gpadilla.mycar.dtos.costoAuto.CostoAutoDto;
import com.gpadilla.mycar.dtos.costoAuto.CostoAutoUpdateDto;
import com.gpadilla.mycar.service.CostoAutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/costos")
@RequiredArgsConstructor
public class CostoAutoController {

    private final CostoAutoService service;

    @PostMapping
    public ResponseEntity<CostoAutoDto> create(@RequestBody CostoAutoCreateDto dto) {
        return ResponseEntity.ok(service.createAndReturnDto(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CostoAutoDto> update(@PathVariable Long id, @RequestBody CostoAutoUpdateDto dto) {
        dto.setId(id);
        return ResponseEntity.ok(service.updateAndReturnDto(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CostoAutoDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findDto(id));
    }

    @GetMapping
    public ResponseEntity<Page<CostoAutoDto>> findAll(Pageable pageable) {
        return ResponseEntity.ok(service.findDtos(pageable));
    }
}
