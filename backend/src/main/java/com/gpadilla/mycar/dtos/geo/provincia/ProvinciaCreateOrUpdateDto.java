package com.gpadilla.mycar.dtos.geo.provincia;

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
public class ProvinciaCreateOrUpdateDto {
    @NotBlank(message = "Debe indicar el nombre de la provincia")
    private String nombre;

    @NotNull(message = "Debe indicar el país")
    private Long paisId;
}
