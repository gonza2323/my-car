package com.gpadilla.mycar.dtos.contacto;

import com.gpadilla.mycar.enums.TipoContacto;
import com.gpadilla.mycar.enums.TipoTelefono;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactoTelefonicoDetailDto {
    private Long id;
    private TipoContacto tipoContacto;
    private String telefono;
    private TipoTelefono tipoTelefono;
    private String observacion;
    private Long empresaId;
    private Long usuarioId;
    private Boolean eliminado;
}
