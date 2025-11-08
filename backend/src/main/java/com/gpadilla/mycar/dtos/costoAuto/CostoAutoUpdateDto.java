package com.gpadilla.mycar.dtos.costoAuto;

import com.gpadilla.mycar.dtos.IdentifiableDto;
import lombok.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CostoAutoUpdateDto extends IdentifiableDto<Long> {
    private Date fechaHasta;
    private double costoTotal;
    private boolean eliminado;
}
