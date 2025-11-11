package com.gpadilla.mycar.controller;

import com.gpadilla.mycar.entity.CaracteristicasAuto;
import com.gpadilla.mycar.pdf.PdfGenerator;
import com.gpadilla.mycar.repository.CaracteristicasAutoRepository;
import com.gpadilla.mycar.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.function.Function;

@RestController
@RequestMapping("/api/v1/reportes")
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class ReporteController {

    private final ReporteService reporteService;
    private final PdfGenerator  pdfGenerator;
    private final CaracteristicasAutoRepository caracteristicasAutoRepository;

    @GetMapping("/recaudacion")
    @PreAuthorize("hasRole('JEFE')")
    public ResponseEntity<byte[]> generarReporte(
            @RequestParam String fechaInicio,
            @RequestParam String fechaFin
    ) {
        byte[] pdfBytes = reporteService.generarReporteRecaudacion(fechaInicio, fechaFin);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte_recaudacion.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    @GetMapping("/modelos")
    @PreAuthorize("hasRole('JEFE')")
    public ResponseEntity<byte[]> generarReporteModelos() {
        var modelos = caracteristicasAutoRepository.findAll();
        System.out.println("Cantidad de modelos encontrados: " + modelos.size());
        modelos.forEach(m -> System.out.println(m.getModelo()));

        var headers = List.of("Modelo");
        var extractors = List.of(
                (Function<CaracteristicasAuto, String>) CaracteristicasAuto::getModelo
        );

        try {
            byte[] pdf = pdfGenerator.generarPdfEnMemoria(
                    "Listado de Modelos de Autos",
                    headers,
                    modelos,
                    extractors
            );

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=modelos_autos.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdf);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error generando el PDF", e);
        }
    }

}

