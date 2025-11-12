package com.gpadilla.mycar.dtos.empresa;

import com.gpadilla.mycar.dtos.geo.direccion.DireccionViewDto;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaDetailDto {

    private Long id;

    private String nombre;

    private String telefonoPrincipal; // se obtiene del contacto

    private String emailPrincipal; // se obtiene del contacto

    private DireccionViewDto direccion;

    private boolean eliminado;
}
