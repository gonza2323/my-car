package com.gpadilla.mycar.dtos.geo.nacionalidad;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NacionalidadCreateOrUpdateDto {
    @NotBlank(message = "Debe indicar el nombre de la nacionalidad")
    private String nombre;
}
