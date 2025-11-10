package com.gpadilla.mycar.controller;

import com.gpadilla.mycar.dtos.empleado.EmpleadoCreateRequestDto;
import com.gpadilla.mycar.dtos.empleado.EmpleadoSummaryDto;
import com.gpadilla.mycar.facade.EmpleadoFacade;
import com.gpadilla.mycar.service.EmpleadoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/empleados")
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class EmpleadoController {
    private final EmpleadoFacade empleadoFacade;
    private final EmpleadoService empleadoService;

    @PostMapping
    @PreAuthorize("hasRole('JEFE')")
    public ResponseEntity<Void> registrarEmpleado(@Valid @RequestBody EmpleadoCreateRequestDto request) {
        empleadoFacade.registrarEmpleado(request);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('JEFE')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        empleadoFacade.borrarEmpleado(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('JEFE')")
    public ResponseEntity<Page<EmpleadoSummaryDto>> find(Pageable pageable) {
        Page<EmpleadoSummaryDto> empleados = empleadoService.findDtos(pageable);
        return ResponseEntity.ok(empleados);
    }
}
