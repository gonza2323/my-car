package com.gpadilla.mycar.controller;

import com.gpadilla.mycar.entity.Factura;
import com.gpadilla.mycar.error.BusinessException;
import com.gpadilla.mycar.repository.FacturaRepository;
import com.gpadilla.mycar.service.FacturaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class FacturaController {

    private final FacturaService facturaService;
    private final FacturaRepository facturaRepository;

    // 👀 Descargar factura PDF asociada a un alquiler
    @GetMapping("/alquileres/{alquilerId}/factura")
    public ResponseEntity<byte[]> descargarFacturaPorAlquiler(@PathVariable Long alquilerId) {
        // 1️⃣ Buscar la factura asociada al alquiler
        Factura factura = facturaRepository.buscarFacturaDeAlquiler(alquilerId)
                .orElseThrow(() -> new BusinessException("No se encontró factura para este alquiler"));

        // 2️⃣ Generar PDF en memoria
        byte[] pdfBytes = facturaService.generarFacturaPdfEnMemoria(factura.getId());

        // 3️⃣ Enviar respuesta como PDF
        String fileName = "factura_" + factura.getNumeroFactura() + ".pdf";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + fileName)
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}