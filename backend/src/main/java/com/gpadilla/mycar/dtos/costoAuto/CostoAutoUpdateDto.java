package com.gpadilla.mycar.dtos.costoAuto;

import lombok.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CostoAutoUpdateDto {
    private Date fechaHasta;
    private double costoTotal;
    private boolean eliminado;
}
