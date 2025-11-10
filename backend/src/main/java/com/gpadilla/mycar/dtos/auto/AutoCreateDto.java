package com.gpadilla.mycar.dtos.auto;

import com.gpadilla.mycar.enums.EstadoAuto;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AutoCreateDto {
    private String patente;
    private EstadoAuto estadoAuto;
    private Long caracteristicasAuto;
}

