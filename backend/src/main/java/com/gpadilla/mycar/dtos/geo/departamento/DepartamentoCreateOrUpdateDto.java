package com.gpadilla.mycar.dtos.geo.departamento;

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
public class DepartamentoCreateOrUpdateDto {
    @NotBlank(message = "Debe indicar el nombre del departamento")
    private String nombre;

    @NotNull(message = "Debe indicar la provincia")
    private Long provinciaId;
}
