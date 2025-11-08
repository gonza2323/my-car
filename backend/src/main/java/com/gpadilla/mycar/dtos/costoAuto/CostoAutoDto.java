package com.gpadilla.mycar.dtos.costoAuto;

import lombok.*;
import lombok.experimental.SuperBuilder;
import java.util.Date;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CostoAutoDto {
    private Long id;
    private Date fechaDesde;
    private Date fechaHasta;
    private double costoTotal;
    private boolean eliminado;

    private Long caracteristicasAutoId;
}

