package com.gpadilla.mycar.dtos.cliente;

import com.gpadilla.mycar.dtos.geo.direccion.DireccionCreateOrUpdateDto;
import com.gpadilla.mycar.enums.TipoDocumento;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteCreateRequestDto {
    @NotBlank(message = "Debe indicar el nombre")
    @Size(max = 50, message = "Máximo 50 caracteres")
    private String nombre;

    @NotBlank(message = "Debe indicar el nombre")
    @Size(max = 50, message = "Máximo 50 caracteres")
    private String apellido;

    @NotNull(message = "Debe indicar la fecha de nacimiento")
    @Past(message = "La fecha debe ser en el pasado")
    private LocalDate fechaNacimiento;

    @NotNull(message = "Debe indicar el tipo de documento")
    private TipoDocumento tipoDocumento;

    @NotBlank(message = "Debe indicar el número de documento")
    @Size(min = 6, max = 20, message = "Entre 6 y 20 caracteres")
    private String numeroDocumento;

    @NotNull(message = "Debe indicar un número de teléfono")
    @Size(min = 6, max = 20, message = "Entre 6 y 20 caracteres")
    private String telefono;

    @NotNull(message = "Debe indicar la nacionalidad")
    private Long nacionalidadId;

    @Valid
    private DireccionCreateOrUpdateDto direccion;

    // para entidad usuario
    @Email(message = "Debe indicar un mail válido")
    private String email;
}
