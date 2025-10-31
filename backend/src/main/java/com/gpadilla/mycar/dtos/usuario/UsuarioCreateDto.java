package com.gpadilla.mycar.dtos.usuario;

import com.gpadilla.mycar.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioCreateDto {
    @Email(message = "Debe indicar un mail válido")
    private String email;

    @NotBlank(message = "Debe indicar una contraseña")
    @Size(min = 8, max = 127, message = "Entre 8 y 127 caracteres")
    private String password;

    @NotBlank(message = "Debe confirmar la contraseña")
    @Size(min = 8, max = 127, message = "Entre 8 y 127 caracteres")
    private String passwordConfirmacion;

    @NotBlank(message = "Debe confirmar la contraseña")
    @Size(min = 8, max = 127, message = "Entre 8 y 127 caracteres")
    private UserRole rol;
}
