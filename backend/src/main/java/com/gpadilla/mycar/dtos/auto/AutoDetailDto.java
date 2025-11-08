package com.gpadilla.mycar.dtos.auto;

import com.gpadilla.mycar.enums.EstadoAuto;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AutoDetailDto {
    private Long id;
    private String patente;
    private EstadoAuto estadoAuto;
    private boolean eliminado;
    private Long caracteristicasAuto;
}
