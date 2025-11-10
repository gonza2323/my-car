package com.gpadilla.mycar.dtos.caracteristicasAuto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CaracteristicasAutoDisponible {
    private Long id;
    private String marca;
    private String modelo;
    private Integer anio;
    private Integer cantidadPuertas;
    private Integer cantidadAsientos;
    private Double precioPorDia;
}
