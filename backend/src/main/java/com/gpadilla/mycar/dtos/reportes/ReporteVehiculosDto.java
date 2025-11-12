package com.gpadilla.mycar.dtos.reportes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class ReporteVehiculosDto {
    private String modelo;
    private String patente;
    private String cliente;
    private LocalDate fechaDesde;
    private LocalDate fechaHasta;
    private Integer diasAlquilado;
    private Double montoTotal;
}
