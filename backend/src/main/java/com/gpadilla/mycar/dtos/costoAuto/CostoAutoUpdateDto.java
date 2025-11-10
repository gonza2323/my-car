package com.gpadilla.mycar.dtos.costoAuto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CostoAutoUpdateDto {
    private LocalDate fechaHasta;
    private double costoTotal;
    private boolean eliminado;
}
