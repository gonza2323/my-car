package com.gpadilla.mycar.dtos.alquiler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlquilerCreateDto {
    private LocalDate fechaDesde;
    private LocalDate fechaHasta;
    private Long clienteId;
    private Long caracteristicaAutoId;
}
