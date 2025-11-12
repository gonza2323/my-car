package com.gpadilla.mycar.dtos.pagos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FormaDePagoDto {
    private Long id;
    private String tipoDePago;  // Ej: "Efectivo", "Tarjeta", "Transferencia"
    private String descripcion; // Opcional, por si querés agregar algo como "Visa - Terminación 1234"
}
