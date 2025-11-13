package com.gpadilla.mycar.service;

import com.gpadilla.mycar.dtos.reportes.RecaudacionAbiertosRow;
import com.gpadilla.mycar.dtos.reportes.RecaudacionCerradosRow;
import com.gpadilla.mycar.dtos.reportes.ReporteRecaudacionDto;
import com.gpadilla.mycar.dtos.reportes.ReporteVehiculosDto;
import com.gpadilla.mycar.pdf.PdfGenerator;
import com.gpadilla.mycar.repository.ReporteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReporteService {

    private final ReporteRepository reporteRepository;
    private final PdfGenerator pdfGenerator;

    public byte[] generarReporteVehiculos(LocalDate fechaInicio, LocalDate fechaFin) {
        List<ReporteVehiculosDto> vehiculosRaw =
                reporteRepository.findVehiculosAlquiladosPorFechas(fechaInicio, fechaFin);

        // ✅ Calcular los días alquilados en Java (portátil y sin errores)
        List<ReporteVehiculosDto> vehiculos = vehiculosRaw.stream()
                .peek(v -> {
                    long dias = ChronoUnit.DAYS.between(v.getFechaDesde(), v.getFechaHasta()) + 1;
                    v.setDiasAlquilado((int) dias);
                })
                .toList();

        return pdfGenerator.generarReporteVehiculosAlquilados(
                fechaInicio, fechaFin, vehiculos
        );
    }


    public byte[] generarReporteRecaudacion(LocalDate fechaInicio, LocalDate fechaFin) {

        // 1️⃣ Consultas separadas
        List<RecaudacionCerradosRow> cerradosRows = reporteRepository.recaudacionCerrados(fechaInicio, fechaFin);
        List<RecaudacionAbiertosRow> abiertosRows = reporteRepository.recaudacionAbiertos(fechaInicio, fechaFin);

        // 2️⃣ Convertir a DTOs de presentación
        List<ReporteRecaudacionDto> cerrados = cerradosRows.stream()
                .map(r -> new ReporteRecaudacionDto(
                        r.modelo(),
                        r.cantidad() != null ? r.cantidad() : 0L,
                        r.total() != null ? r.total() : 0.0,
                        0.0
                ))
                .toList();

        // 🔹 Calcular el total abierto según la duración y costo/día
        List<ReporteRecaudacionDto> abiertos = abiertosRows.stream()
                .map(r -> {
                    long dias = ChronoUnit.DAYS.between(
                            r.desde().isBefore(fechaInicio) ? fechaInicio : r.desde(),
                            r.hasta().isAfter(fechaFin) ? fechaFin : r.hasta()
                    ) + 1;

                    double totalAbierto = dias * (r.costoPorDia() != null ? r.costoPorDia() : 0.0);

                    return new ReporteRecaudacionDto(
                            r.modelo(),
                            0L,
                            0.0,
                            totalAbierto
                    );
                })
                .toList();

        // 3️⃣ Calcular totales globales
        double totalCerrados = cerrados.stream()
                .mapToDouble(ReporteRecaudacionDto::getTotalCerrados)
                .sum();

        double totalAbiertos = abiertos.stream()
                .mapToDouble(ReporteRecaudacionDto::getTotalAbiertos)
                .sum();

        // 4️⃣ Generar PDF (exactamente igual que antes)
        return pdfGenerator.generarReporteRecaudacionConDetalle(
                fechaInicio, fechaFin, cerrados, abiertos, totalCerrados, totalAbiertos
        );
    }
}

