package com.gpadilla.mycar.dtos.factura;

import com.gpadilla.mycar.dtos.alquiler.AlquilerDto;
import com.gpadilla.mycar.dtos.promocion.PromocionViewDto;
import lombok.Data;

@Data
public class DetalleFacturaDto {
    private Double subtotal;
    private AlquilerDto alquiler;
    private PromocionViewDto promocion;
}

