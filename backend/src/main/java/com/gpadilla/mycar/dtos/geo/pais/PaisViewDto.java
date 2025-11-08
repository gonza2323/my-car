package com.gpadilla.mycar.dtos.geo.pais;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaisViewDto {
    private Long id;
    private String nombre;
}
