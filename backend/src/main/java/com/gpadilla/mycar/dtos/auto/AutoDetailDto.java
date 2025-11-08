package com.gpadilla.mycar.dtos.auto;

import com.gpadilla.mycar.dtos.IdentifiableDto;
import com.gpadilla.mycar.entity.EstadoAuto;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AutoDetailDto extends IdentifiableDto<Long> {
    private String patente;
    private EstadoAuto estadoAuto;
    private boolean eliminado;
    private Long caracteristicasAuto;
}
