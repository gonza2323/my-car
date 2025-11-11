package com.gpadilla.mycar.service;

import com.gpadilla.mycar.dtos.reportes.ReporteRecaudacionDto;
import com.gpadilla.mycar.pdf.PdfGenerator;
import com.gpadilla.mycar.repository.ReporteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ReporteService {

    private final ReporteRepository reporteRepository;
    private final PdfGenerator pdfGenerator;

    public byte[] generarReporteRecaudacion(String fechaInicio, String fechaFin) {

        // 1️⃣ Obtener datos desde la BD
        List<ReporteRecaudacionDto> reportes = reporteRepository.findByFechas(fechaInicio, fechaFin);

        // 2️⃣ Encabezados y extractores
        List<String> headers = List.of("Modelo", "Fecha", "Monto Recaudado");

        List<Function<ReporteRecaudacionDto, String>> extractors = List.of(
                ReporteRecaudacionDto::getModeloAuto,
                ReporteRecaudacionDto::getFecha,
                r -> "$ " + r.getMontoRecaudado()
        );

        // 3️⃣ Generar PDF en memoria
        return pdfGenerator.generarPdfEnMemoria(
                "Reporte de Recaudación",
                headers,
                reportes,
                extractors
        );
    }
}

