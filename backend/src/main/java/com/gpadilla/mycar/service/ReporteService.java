package com.gpadilla.mycar.service;

import com.gpadilla.mycar.dtos.reportes.RecaudacionAbiertosRow;
import com.gpadilla.mycar.dtos.reportes.RecaudacionCerradosRow;
import com.gpadilla.mycar.dtos.reportes.ReporteRecaudacionDto;
import com.gpadilla.mycar.dtos.reportes.ReporteVehiculosDto;
import com.gpadilla.mycar.excel.ExcelGenerator;
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
    private final ExcelGenerator excelGenerator;

    public byte[] generarReporteVehiculos(LocalDate fechaInicio, LocalDate fechaFin) {
        List<ReporteVehiculosDto> vehiculosRaw =
                reporteRepository.findVehiculosAlquiladosPorFechas(fechaInicio, fechaFin);

        // Calcular los días alquilados en Java
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

    public byte[] generarReporteVehiculosExcel(LocalDate fechaInicio, LocalDate fechaFin) {


        List<ReporteVehiculosDto> vehiculosRaw =
                reporteRepository.findVehiculosAlquiladosPorFechas(fechaInicio, fechaFin);


        List<ReporteVehiculosDto> vehiculos = vehiculosRaw.stream()
                .peek(v -> {
                    long dias = ChronoUnit.DAYS.between(v.getFechaDesde(), v.getFechaHasta()) + 1;
                    v.setDiasAlquilado((int) dias);
                })
                .toList();

        // 3) Definir encabezados de columnas
        List<String> headers = List.of(
                "Modelo",
                "Patente",
                "Cliente",
                "Desde",
                "Hasta",
                "Días alquilado",
                "Monto"
        );

        // 4) Mapear cada fila del DTO a Strings
        return excelGenerator.generarExcelEnMemoria(
                "Vehículos alquilados " + fechaInicio + " al " + fechaFin,
                headers,
                vehiculos,
                List.of(
                        v -> v.getModelo(),                          // Modelo
                        v -> v.getPatente(),                         // Patente
                        v -> v.getClienteNombre(),                   // Nombre cliente
                        v -> v.getFechaDesde().toString(),           // Desde
                        v -> v.getFechaHasta().toString(),           // Hasta
                        v -> String.valueOf(v.getDiasAlquilado()),   // Días
                        v -> String.valueOf(v.getMonto())            // Monto
                )
        );
    }

    public byte[] generarReporteRecaudacionExcel(LocalDate fechaInicio, LocalDate fechaFin) {

        // 1) Consultas separadas (igual que en el PDF)
        List<RecaudacionCerradosRow> cerradosRows = reporteRepository.recaudacionCerrados(fechaInicio, fechaFin);
        List<RecaudacionAbiertosRow> abiertosRows = reporteRepository.recaudacionAbiertos(fechaInicio, fechaFin);

        // 2) Convertir a DTOs de presentación (mismo código que en generarReporteRecaudacion)
        List<ReporteRecaudacionDto> cerrados = cerradosRows.stream()
                .map(r -> new ReporteRecaudacionDto(
                        r.modelo(),
                        r.cantidad() != null ? r.cantidad() : 0L,
                        r.total() != null ? r.total() : 0.0,
                        0.0
                ))
                .toList();

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

        // 3) Totales globales (igual que en la versión PDF)
        double totalCerrados = cerrados.stream()
                .mapToDouble(ReporteRecaudacionDto::getTotalCerrados)
                .sum();

        double totalAbiertos = abiertos.stream()
                .mapToDouble(ReporteRecaudacionDto::getTotalAbiertos)
                .sum();

        long cantidadCerradosTotal = cerrados.stream()
                .mapToLong(ReporteRecaudacionDto::getCantidadCerrados)
                .sum();

        double totalGeneral = totalCerrados + totalAbiertos;

        // 4) Armar la lista final de filas para el Excel
        List<ReporteRecaudacionDto> filas = new ArrayList<>();
        filas.addAll(cerrados);
        filas.addAll(abiertos);

        // Fila de total al final
        filas.add(new ReporteRecaudacionDto(
                "TOTAL GENERAL",
                cantidadCerradosTotal,
                totalCerrados,
                totalAbiertos
        ));

        // 5) Definir encabezados y generar Excel con el ExcelGenerator
        List<String> headers = List.of(
                "Modelo",
                "Cant. Cerrados",
                "Total Cerrados",
                "Total Abiertos",
                "Total General"
        );

        return excelGenerator.generarExcelEnMemoria(
                String.format("Reporte de Recaudación (%s a %s)", fechaInicio, fechaFin),
                headers,
                filas,
                List.of(
                        d -> d.getModelo(),
                        d -> String.valueOf(d.getCantidadCerrados()),
                        d -> String.format("$ %.2f", d.getTotalCerrados()),
                        d -> String.format("$ %.2f", d.getTotalAbiertos()),
                        d -> String.format("$ %.2f", d.getTotalGeneral())
                )
        );
    }

}

