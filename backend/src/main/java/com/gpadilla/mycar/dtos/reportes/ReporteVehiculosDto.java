package com.gpadilla.mycar.dtos.reportes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReporteVehiculosDto {
    private String modelo;
    private String patente;
    private String clienteNombre;
    private LocalDate fechaDesde;
    private LocalDate fechaHasta;
    private Integer diasAlquilado;
    private Double monto;

    // 👇 Constructor usado por la query JPQL
    public ReporteVehiculosDto(String modelo, String patente, String clienteNombre,
                               LocalDate fechaDesde, LocalDate fechaHasta, Double monto) {
        this.modelo = modelo;
        this.patente = patente;
        this.clienteNombre = clienteNombre;
        this.fechaDesde = fechaDesde;
        this.fechaHasta = fechaHasta;
        this.monto = monto;
    }
}
