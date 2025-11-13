package com.gpadilla.mycar.controller;

import com.gpadilla.mycar.dtos.imagen.ImagenCreateDto;
import com.gpadilla.mycar.dtos.imagen.ImagenDetailDto;
import com.gpadilla.mycar.service.ImagenService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class ImagenController {

    private final ImagenService service;

    @PostMapping("/imagenes")
    public ResponseEntity<ImagenDetailDto> create(@RequestBody ImagenCreateDto dto) {
        return ResponseEntity.ok(service.createAndReturnDto(dto));
    }

    @DeleteMapping("/imagenes/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/imagenes/{id}")
    public ResponseEntity<ImagenDetailDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findDto(id));
    }

    @GetMapping("/imagenes")
    public ResponseEntity<Page<ImagenDetailDto>> findAll(Pageable pageable) {
        return ResponseEntity.ok(service.findDtos(pageable));
    }

    // 🔹 Obtener imagen binaria (raw)
    @GetMapping("vehiculos/{vehicleId}/imagen")
    public ResponseEntity<byte[]> obetnerImagenDeVehiculo(@PathVariable Long vehicleId) {
        ImagenDetailDto imagen = service.findByVehicleId(vehicleId);  // obtiene la entidad real

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + imagen.getNombre() + "\"")
                .contentType(MediaType.parseMediaType(imagen.getMime()))
                .body(imagen.getContenido());
    }
}

