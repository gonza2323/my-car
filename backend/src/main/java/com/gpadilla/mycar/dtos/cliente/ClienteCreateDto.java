package com.gpadilla.mycar.dtos.cliente;

import com.gpadilla.mycar.enums.TipoDocumento;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteCreateDto {
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
    private TipoDocumento tipoDocumento;
    private String numeroDocumento;
    private Long nacionalidadId;
}
