package com.gpadilla.mycar.dtos.factura;

import com.gpadilla.mycar.dtos.pagos.FormaDePagoDto;
import com.gpadilla.mycar.enums.EstadoFactura;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class FacturaDto {
    private Long id;
    private Long numeroFactura;
    private LocalDate fechaFactura;
    private Double totalPagado;
    private EstadoFactura estado;
    private FormaDePagoDto formaDePago;
    private List<DetalleFacturaDto> detalles;
}
