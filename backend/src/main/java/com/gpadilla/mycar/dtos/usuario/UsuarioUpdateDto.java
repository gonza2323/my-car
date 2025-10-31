package com.gpadilla.mycar.dtos.usuario;

import com.gpadilla.mycar.dtos.IdentifiableDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioUpdateDto extends IdentifiableDto<Long> {
    @Email(message = "Debe indicar un mail válido")
    private String email;
}
