package com.gpadilla.mycar.service;

import com.gpadilla.mycar.dtos.reportes.ReporteRecaudacionDto;
import com.gpadilla.mycar.dtos.reportes.ReporteVehiculosDto;
import com.gpadilla.mycar.pdf.PdfGenerator;
import com.gpadilla.mycar.repository.ReporteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReporteService {

    private final ReporteRepository reporteRepository;
    private final PdfGenerator pdfGenerator;

    public byte[] generarReporteVehiculos(LocalDate fechaInicio, LocalDate fechaFin) {
        List<ReporteVehiculosDto> vehiculos = reporteRepository.findVehiculosAlquiladosPorFechas(fechaInicio, fechaFin);

        return pdfGenerator.generarReporteVehiculosAlquilados(
                fechaInicio, fechaFin, vehiculos
        );
    }



    public byte[] generarReporteRecaudacion(LocalDate fechaInicio, LocalDate fechaFin) {

        List<Object[]> resultados = reporteRepository.findByFechas(fechaInicio, fechaFin);

        List<ReporteRecaudacionDto> reportes = resultados.stream()
                .map(r -> new ReporteRecaudacionDto(
                        (String) r[0],                                 // modelo
                        r[1] != null ? ((Number) r[1]).longValue() : 0L,   // cantidadCerrados
                        r[2] != null ? ((Number) r[2]).doubleValue() : 0.0, // totalCerrados
                        r[3] != null ? ((Number) r[3]).doubleValue() : 0.0  // totalAbiertos
                ))
                .collect(Collectors.toList());

        List<ReporteRecaudacionDto> cerrados = new ArrayList<>();
        List<ReporteRecaudacionDto> abiertos = new ArrayList<>();

        for (ReporteRecaudacionDto dto : reportes) {
            boolean tieneCerrados = dto.getTotalCerrados() != null && dto.getTotalCerrados() > 0;
            boolean tieneAbiertos = dto.getTotalAbiertos() != null && dto.getTotalAbiertos() > 0;

            if (tieneCerrados) cerrados.add(dto);
            if (tieneAbiertos) abiertos.add(dto);
        }

        double totalCerrados = cerrados.stream()
                .mapToDouble(r -> r.getTotalCerrados() != null ? r.getTotalCerrados() : 0)
                .sum();

        double totalAbiertos = abiertos.stream()
                .mapToDouble(r -> r.getTotalAbiertos() != null ? r.getTotalAbiertos() : 0)
                .sum();


        return pdfGenerator.generarReporteRecaudacionConDetalle(
                fechaInicio, fechaFin, cerrados, abiertos, totalCerrados, totalAbiertos
        );
    }



}

