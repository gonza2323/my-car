package com.gpadilla.mycar.dtos.pagos;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    private Long alquilerId;
}
