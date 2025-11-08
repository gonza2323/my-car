package com.gpadilla.mycar.dtos.empleado;

import com.gpadilla.mycar.enums.TipoDocumento;
import com.gpadilla.mycar.enums.TipoEmpleado;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmpleadoCreateDto {
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
    private TipoDocumento tipoDocumento;
    private String numeroDocumento;
    private TipoEmpleado tipoEmpleado;
}
