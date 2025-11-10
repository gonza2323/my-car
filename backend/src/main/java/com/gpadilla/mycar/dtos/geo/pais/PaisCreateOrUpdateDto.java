package com.gpadilla.mycar.dtos.geo.pais;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaisCreateOrUpdateDto {
    @NotBlank(message = "Debe indicar el nombre del país")
    private String nombre;
}
