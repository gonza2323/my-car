package com.gpadilla.mycar.dtos.caracteristicasAuto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CaracteristicasAutoSummaryDto {
    private Long id;
    private String marca;
    private String modelo;
    private int anio;
}
