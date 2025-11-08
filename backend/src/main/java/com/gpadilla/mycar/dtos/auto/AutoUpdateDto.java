package com.gpadilla.mycar.dtos.auto;

import com.gpadilla.mycar.dtos.IdentifiableDto;
import com.gpadilla.mycar.entity.EstadoAuto;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AutoUpdateDto extends IdentifiableDto<Long> {
    private EstadoAuto estadoAuto;
    private boolean eliminado;
}
