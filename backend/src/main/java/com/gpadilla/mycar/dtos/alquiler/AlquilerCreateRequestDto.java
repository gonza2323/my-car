package com.gpadilla.mycar.dtos.alquiler;

import com.gpadilla.mycar.enums.TipoDePago;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlquilerCreateRequestDto {
    private LocalDate fechaDesde;
    private LocalDate fechaHasta;
    private Long clienteId;
    private Long caracteristicaAutoId;
    private String codigoDescuento;
    private TipoDePago formaDePago;
}
