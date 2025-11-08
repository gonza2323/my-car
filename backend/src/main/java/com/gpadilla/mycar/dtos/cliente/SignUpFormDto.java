package com.gpadilla.mycar.dtos.cliente;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpFormDto {
    @Email(message = "Debe indicar un mail válido")
    private String email;

    @NotNull(message = "Debe indicar una contraseña")
    @Size(min = 8, max = 255, message = "Entre 8 y 255 caracteres")
    private String password;

    @NotNull(message = "Debe confirmar la contraseña")
    private String passwordConfirm;
}