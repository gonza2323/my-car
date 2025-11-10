package com.gpadilla.mycar.dtos.caracteristicasAuto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaracteristicasAutoUpdateDto {
    private String marca;
    private String modelo;
    private int cantidadPuertas;
    private int cantidadAsientos;
    private int anio;
}
