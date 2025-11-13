package com.gpadilla.mycar.dtos.costoAuto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CostoAutoDto {
    private Long id;
    private LocalDate fechaDesde;
    private LocalDate fechaHasta;
    private double costoTotal;
    private Long caracteristicasAutoId;
}

