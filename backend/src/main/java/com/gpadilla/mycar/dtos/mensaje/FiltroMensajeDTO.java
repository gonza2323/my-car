package com.gpadilla.mycar.dtos.mensaje;

import com.gpadilla.mycar.enums.TipoMensaje;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FiltroMensajeDTO {
    private TipoMensaje tipoMensaje;
    private String asuntoContiene;
    private String nombreUsuario;
}
