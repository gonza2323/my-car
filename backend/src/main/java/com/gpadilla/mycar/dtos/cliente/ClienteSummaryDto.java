package com.gpadilla.mycar.dtos.cliente;

import com.gpadilla.mycar.enums.TipoDocumento;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteSummaryDto {
    private Long id;
    private String nombre;
    private String apellido;
    private TipoDocumento tipoDocumento;
    private String numeroDocumento;
    private String nacionalidadNombre;
    private String usuarioEmail;
}
