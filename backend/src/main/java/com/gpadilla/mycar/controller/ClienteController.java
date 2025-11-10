package com.gpadilla.mycar.controller;

import com.gpadilla.mycar.dtos.cliente.ClienteCreateRequestDto;
import com.gpadilla.mycar.dtos.cliente.ClienteSummaryDto;
import com.gpadilla.mycar.facade.ClienteFacade;
import com.gpadilla.mycar.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class ClienteController {
    private final ClienteFacade clienteFacade;
    private final ClienteService clienteService;

    @PostMapping
    @PreAuthorize("hasAnyRole('JEFE', 'ADMINISTRATIVO')")
    public ResponseEntity<Void> registrarCliente(@Valid @RequestBody ClienteCreateRequestDto request) {
        clienteFacade.registrarClientePorFormularioAdmin(request);
        return ResponseEntity.ok(null);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('JEFE', 'ADMINISTRATIVO')")
    public ResponseEntity<Page<ClienteSummaryDto>> find(Pageable pageable) {
        Page<ClienteSummaryDto> clientes = clienteService.findDtos(pageable);
        return ResponseEntity.ok(clientes);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('JEFE')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        clienteFacade.borrarCliente(id);
        return ResponseEntity.noContent().build();
    }
}
