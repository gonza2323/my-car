package com.gpadilla.mycar.dtos.alquiler;

import com.gpadilla.mycar.dtos.auto.AutoDetailDto;
import com.gpadilla.mycar.dtos.cliente.ClienteSummaryDto;
import com.gpadilla.mycar.enums.EstadoPagoAlquiler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlquilerDetalleDto {
    private Long id;
    private EstadoPagoAlquiler estado;
    private LocalDate fechaDesde;
    private LocalDate fechaHasta;
    private Double costoPorDia;
    private Double subtotal;
    private Double porcentajeDescuento;
    private Double cantidadDescuento;
    private Double total;
    private AutoDetailDto vehiculo;
    private ClienteSummaryDto cliente;
}
