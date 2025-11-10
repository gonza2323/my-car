package com.gpadilla.mycar.dtos.geo.nacionalidad;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NacionalidadViewDto {
    private Long id;
    private String nombre;
}
