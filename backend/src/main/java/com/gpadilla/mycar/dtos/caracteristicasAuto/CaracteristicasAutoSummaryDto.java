package com.gpadilla.mycar.dtos.caracteristicasAuto;

import com.gpadilla.mycar.dtos.IdentifiableDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CaracteristicasAutoSummaryDto extends IdentifiableDto<Long> {
    private String marca;
    private String modelo;
    private int anio;
}
