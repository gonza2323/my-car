package com.gpadilla.mycar.dtos.imagen;

import com.gpadilla.mycar.enums.TipoImagen;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ImagenDetailDto {
    private Long id;
    private String nombre;
    private String mime;
    private boolean eliminada;
    private TipoImagen tipo;
}

