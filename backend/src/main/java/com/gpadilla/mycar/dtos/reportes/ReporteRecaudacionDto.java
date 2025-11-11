package com.gpadilla.mycar.dtos.reportes;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReporteRecaudacionDto {
    private String modeloAuto;
    private String fecha;
    private Double montoRecaudado;
}