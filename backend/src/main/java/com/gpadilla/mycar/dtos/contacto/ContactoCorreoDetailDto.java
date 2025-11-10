package com.gpadilla.mycar.dtos.contacto;

import com.gpadilla.mycar.enums.TipoContacto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactoCorreoDetailDto {
    private Long id;
    private TipoContacto tipoContacto;
    private String email;
    private String observacion;
    private Long empresaId;
    private Long personaId;
}
