package com.gpadilla.mycar.dtos.pagos;

import com.gpadilla.mycar.enums.TipoDePago;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManualPaymentRequest {
    private Long alquilerId;
    private TipoDePago metodo;
}
