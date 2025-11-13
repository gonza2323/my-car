package com.gpadilla.mycar.dtos.alquiler;

import com.gpadilla.mycar.dtos.auto.AutoSummaryDto;
import com.gpadilla.mycar.dtos.cliente.ClienteSummaryDto;
import com.gpadilla.mycar.enums.EstadoPagoAlquiler;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AlquilerDto {
    private LocalDate fechaDesde;
    private LocalDate fechaHasta;
    private AutoSummaryDto auto;
    private ClienteSummaryDto cliente;
    private EstadoPagoAlquiler estado;
}
