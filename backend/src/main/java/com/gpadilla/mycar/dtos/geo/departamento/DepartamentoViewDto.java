package com.gpadilla.mycar.dtos.geo.departamento;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartamentoViewDto {
    private Long id;
    private String nombre;

    private Long provinciaId;
    private String provinciaNombre;

    private Long paisId;
    private String paisNombre;
}
