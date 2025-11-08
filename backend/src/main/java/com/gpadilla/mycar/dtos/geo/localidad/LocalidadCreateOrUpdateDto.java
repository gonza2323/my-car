package com.gpadilla.mycar.dtos.geo.localidad;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocalidadCreateOrUpdateDto {
    @NotBlank(message = "Debe indicar el nombre de la localidad")
    private String nombre;

    @NotNull(message = "Debe indicar el departamento")
    private Long departamentoId;
}
