package com.gpadilla.mycar.dtos.contacto;


import com.gpadilla.mycar.enums.TipoContacto;
import com.gpadilla.mycar.enums.TipoTelefono;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContactoCreateDto {

    @NonNull
    private TipoContacto tipoContacto;

    @NotBlank(message = "El teléfono es obligatorio.")
    private String telefono;

    @NonNull
    private TipoTelefono tipoTelefono;

    private String email; //no lo pongo obligatorio porque ya lo tendriamos del registro

    @Builder.Default
    private String observacion = "";
}
