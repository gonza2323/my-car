package com.gpadilla.mycar.dtos.auto;

import com.gpadilla.mycar.entity.EstadoAuto;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AutoUpdateDto {
    private EstadoAuto estadoAuto;
    private boolean eliminado;
}
