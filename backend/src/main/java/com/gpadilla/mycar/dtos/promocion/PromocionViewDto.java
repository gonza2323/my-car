package com.gpadilla.mycar.dtos.promocion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PromocionViewDto {
    private Long id;
    private Double porcentajeDescuento;
    private String codigoDescuento;
    private String descripcion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
}
