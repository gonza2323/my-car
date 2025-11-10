package com.gpadilla.mycar.dtos.empleado;

import com.gpadilla.mycar.enums.TipoEmpleado;

public interface EmpleadoSummaryDto {
    Long getId();
    String getNombre();
    String getApellido();
    String getTipoDocumento();
    String getNumeroDocumento();
    TipoEmpleado getTipoEmpleado();
    String getUsuarioEmail();
}
