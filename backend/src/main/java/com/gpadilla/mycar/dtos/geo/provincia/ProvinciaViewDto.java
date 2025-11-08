package com.gpadilla.mycar.dtos.geo.provincia;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProvinciaViewDto {
    private Long id;
    private String nombre;

    private Long paisId;
    private Long paisNombre;
}
