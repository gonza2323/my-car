package com.gpadilla.mycar.dtos.contacto;


import com.gpadilla.mycar.enums.TipoContacto;
import com.gpadilla.mycar.enums.TipoTelefono;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContactoCreateOrUpdateDto {

    @Builder.Default
    private TipoContacto tipoContacto = TipoContacto.LABORAL;

    @Pattern(regexp = "^\\+?[0-9\\s\\-()]{6,25}$", message = "El teléfono debe tener un formato válido")
    private String telefono;

    private TipoTelefono tipoTelefono;

    @Size(max = 150, message = "El email no puede superar los 150 caracteres")
    @Email(message = "Debe indicar un email válido")
    private String email;

    @Builder.Default
    @Size(max = 200, message = "La observación no puede superar los 200 caracteres")
    private String observacion = "";
}
