package com.gpadilla.mycar.dtos.costoAuto;

import lombok.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CostoAutoCreateDto {
    private Date fechaDesde;
    private Date fechaHasta;
    private double costoTotal;
    private Long caracteristicasAutoId;
}

