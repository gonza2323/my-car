package com.gpadilla.mycar.dtos.geo.direccion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DireccionViewDto {
    private String calle;
    private String numeracion;
    private String barrio;
    private String manzanaPiso;
    private String casaDepartamento;
    private String referencia;

    private Long localidadId;
    private String localidadNombre;

    private Long departamentoId;
    private String departamentoNombre;

    private Long provinciaId;
    private String provinciaNombre;

    private Long paisId;
    private String paisNombre;
}
