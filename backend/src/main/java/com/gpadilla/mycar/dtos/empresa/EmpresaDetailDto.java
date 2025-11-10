package com.gpadilla.mycar.dtos.empresa;

import com.gpadilla.mycar.dtos.geo.direccion.DireccionViewDto;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmpresaDetailDto {

    private Long id;
    private String nombre;

    private String telefonoPrincipal;
    private String emailPrincipal;

    private DireccionViewDto direccion;
}
