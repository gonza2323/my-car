package com.gpadilla.mycar.dtos.imagen;

import com.gpadilla.mycar.entity.TipoImagen;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImagenCreateDto {
    private String nombre;
    private String mime;
    private byte[] contenido; // o String url
    private TipoImagen tipo;
    private Long caracteristicasAutoId;
}
