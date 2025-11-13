package com.gpadilla.mycar.controller;

import com.gpadilla.mycar.dtos.alquiler.AlquilerDto;
import com.gpadilla.mycar.dtos.auto.AutoSummaryDto;
import com.gpadilla.mycar.dtos.factura.DetalleFacturaDto;
import com.gpadilla.mycar.dtos.factura.FacturaDto;
import com.gpadilla.mycar.entity.*;
import com.gpadilla.mycar.error.BusinessException;
import com.gpadilla.mycar.mapper.FacturaMapper;
import com.gpadilla.mycar.repository.FacturaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class test {

    private final FacturaRepository facturaRepository;
    private final FacturaMapper facturaMapper;   // ❗ Te faltaba esto

    @GetMapping("/debug-factura/{alquilerId}")
    public ResponseEntity<String> debugFactura(@PathVariable Long alquilerId) {

        // 1) Obtener entidad COMPLETA con JOIN FETCH (igual que el PDF)
        Factura facturaEntity = facturaRepository.findFacturaCompletaPorAlquiler(alquilerId)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));

        // 2) Convertir a DTO (igual que el PDF)
        FacturaDto facturaDto = facturaMapper.toDto(facturaEntity);

        // 3) Reproducir EXACTAMENTE el mismo recorrido que hace el PDF
        DetalleFacturaDto det = facturaDto.getDetalles().get(0);
        AlquilerDto alquiler = det.getAlquiler();
        AutoSummaryDto auto = alquiler.getAuto();

        // --- DEBUG ---
        System.out.println("MARCA DTO = " + (auto != null ? auto.getMarca() : "NULL"));
        System.out.println("MODELO DTO = " + (auto != null ? auto.getModelo() : "NULL"));

        // 4) Armar respuesta similar al debug original, pero con DTOs
        String resultado = """
            --- DEBUG FACTURA DTO ---
            Factura ID: %d

            AUTO (DTO):
              Marca:  %s
              Modelo: %s
              Año:    %s

            DETALLE:
              Subtotal: %.2f

            """.formatted(
                facturaDto.getId(),
                auto != null ? auto.getMarca() : "NULL",
                auto != null ? auto.getModelo() : "NULL",
                auto != null ? auto.getAnio() : "NULL",
                det.getSubtotal()
        );

        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/debug-factura-hard/{alquilerId}")
    public ResponseEntity<String> debugHard(@PathVariable Long alquilerId) {

        System.out.println("========== DEBUG NIVEL ENTIDAD ==========");

        Factura facturaEntity = facturaRepository
                .findFacturaCompletaPorAlquiler(alquilerId)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));

        // 1) Detalle
        DetalleFactura detEntity = facturaEntity.getDetalles().get(0);
        System.out.println("DetalleFactura ENTITY: " + detEntity);

        Alquiler aEntity = detEntity.getAlquiler();
        System.out.println("Alquiler ENTITY: " + aEntity);

        Auto autoEntity = aEntity.getAuto();
        System.out.println("Auto ENTITY: " + autoEntity);

        CaracteristicasAuto caEntity = autoEntity.getCaracteristicasAuto();
        System.out.println("CaracteristicasAuto ENTITY: " + caEntity);

        if (caEntity != null) {
            System.out.println("ENTITY MARCA: " + caEntity.getMarca());
            System.out.println("ENTITY MODELO: " + caEntity.getModelo());
            System.out.println("ENTITY ANIO: " + caEntity.getAnio());
        } else {
            System.out.println("ENTITY: caracteristicasAuto ES NULL ❌❌❌");
        }

        System.out.println("\n========== DEBUG NIVEL DTO ==========");

        FacturaDto facturaDto = facturaMapper.toDto(facturaEntity);

        DetalleFacturaDto detDto = facturaDto.getDetalles().get(0);
        AlquilerDto aDto = detDto.getAlquiler();
        AutoSummaryDto autoDto = aDto.getAuto();

        System.out.println("Auto DTO: " + autoDto);

        if (autoDto != null) {
            System.out.println("DTO MARCA: " + autoDto.getMarca());
            System.out.println("DTO MODELO: " + autoDto.getModelo());
            System.out.println("DTO ANIO: " + autoDto.getAnio());
        } else {
            System.out.println("DTO AUTO ES NULL ❌❌❌");
        }

        return ResponseEntity.ok("Mirar consola");
    }


}
