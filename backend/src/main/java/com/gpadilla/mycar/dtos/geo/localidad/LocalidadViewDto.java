package com.gpadilla.mycar.dtos.geo.localidad;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocalidadViewDto {
    private Long id;
    private String nombre;

    private Long departamentoId;
    private String departamentoNombre;

    private Long provinciaId;
    private String provinciaNombre;

    private Long paisId;
    private String paisNombre;
}
