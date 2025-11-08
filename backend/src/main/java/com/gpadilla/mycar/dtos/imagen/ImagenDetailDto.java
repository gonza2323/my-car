package com.gpadilla.mycar.dtos.imagen;

import com.gpadilla.mycar.dtos.IdentifiableDto;
import com.gpadilla.mycar.entity.TipoImagen;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ImagenDetailDto extends IdentifiableDto<Long> {
    private String nombre;
    private String mime;
    private boolean eliminada;
    private TipoImagen tipo;
}

