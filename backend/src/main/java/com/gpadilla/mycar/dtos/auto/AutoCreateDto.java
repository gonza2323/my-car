package com.gpadilla.mycar.dtos.auto;

import com.gpadilla.mycar.entity.EstadoAuto;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AutoCreateDto {
    private String patente;
    private EstadoAuto estadoAuto;
    private Long caracteristicasAuto;
}

