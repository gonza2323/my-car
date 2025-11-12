package com.gpadilla.mycar.dtos.reportes;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ReporteRecaudacionDto {
    private String modelo;             // "Fiat Cronos"
    private Long cantidadCerrados;     // cuántos cerrados
    private Double totalCerrados;      // $ cerrados
    private Double totalAbiertos;      // $ abiertos (prorrateo)

    public Double getTotalGeneral() {
        double totalC = totalCerrados != null ? totalCerrados : 0;
        double totalA = totalAbiertos != null ? totalAbiertos : 0;
        return totalC + totalA;
    }
}