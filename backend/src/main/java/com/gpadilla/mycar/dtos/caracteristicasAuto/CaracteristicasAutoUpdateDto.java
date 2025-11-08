package com.gpadilla.mycar.dtos.caracteristicasAuto;

import com.gpadilla.mycar.dtos.IdentifiableDto;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaracteristicasAutoUpdateDto extends IdentifiableDto<Long> {
    private int cantTotalAutos;
    private int cantidadAlquilados;
}
