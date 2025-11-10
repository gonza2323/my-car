package com.gpadilla.mycar.dtos.empresa;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaCreateOrUpdateDto{

    @NotBlank
    @Size(max = 100)
    private String nombre;

    @NotNull
    private Long direccionId;
}
