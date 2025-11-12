package com.gpadilla.mycar.controller;

import com.gpadilla.mycar.dtos.factura.FacturaDto;
import com.gpadilla.mycar.pdf.PdfGenerator;
import com.gpadilla.mycar.service.FacturaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/facturas")
@RequiredArgsConstructor
public class FacturaController {

    private final FacturaService facturaService;
    private final PdfGenerator pdfGenerator;

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> generarFacturaPdf(@PathVariable Long id) {
        var facturaDto = facturaService.buscarFacturaDto(id);
        byte[] pdfBytes = pdfGenerator.generarFacturaPdf(facturaDto);

        String fileName = "factura_" + facturaDto.getNumeroFactura() + ".pdf";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}
