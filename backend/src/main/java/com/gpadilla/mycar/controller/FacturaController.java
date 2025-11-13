package com.gpadilla.mycar.controller;

import com.gpadilla.mycar.entity.Factura;
import com.gpadilla.mycar.entity.FacturaArchivo;
import com.gpadilla.mycar.error.BusinessException;
import com.gpadilla.mycar.pdf.PdfGenerator;
import com.gpadilla.mycar.repository.FacturaArchivoRepository;
import com.gpadilla.mycar.repository.FacturaRepository;
import com.gpadilla.mycar.service.FacturaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/v1/facturas")
@RequiredArgsConstructor
public class FacturaController {

    private final FacturaService facturaService;
    private final FacturaRepository facturaRepository;

    // 👀 Descargar factura PDF asociada a un alquiler
    @GetMapping("/alquiler/{alquilerId}/pdf")
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