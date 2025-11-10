package com.gpadilla.mycar.dtos.auto;

import com.gpadilla.mycar.enums.EstadoAuto;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AutoDetailDto {
    private Long id;
    private String patente;
    private EstadoAuto estadoAuto;

    private Long caracteristicaAutoId;
    private String marca;
    private String modelo;
    private int cantidadPuertas;
    private int cantidadAsientos;
    private int anio;
    private int cantTotalAutos;
}
