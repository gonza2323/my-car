package com.gpadilla.mycar.dtos.empresa;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
    @Size(max = 20)
    private String telefonoPrincipal;

    @NotBlank
    @Email
    @Size(max = 100)
    private String emailPrincipal;
}
