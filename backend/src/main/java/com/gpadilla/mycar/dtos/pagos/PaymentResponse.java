package com.gpadilla.mycar.dtos.pagos;

import com.gpadilla.mycar.enums.EstadoPagoAlquiler;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private Long alquilerId;
    private String urlDePago;
    private EstadoPagoAlquiler status;
}
