package com.gpadilla.mycar.dtos.caracteristicasAuto;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CaracteristicasAutoCreateDto {
    private String marca;
    private String modelo;
    private int cantidadPuertas;
    private int cantidadAsientos;
    private int anio;
}
