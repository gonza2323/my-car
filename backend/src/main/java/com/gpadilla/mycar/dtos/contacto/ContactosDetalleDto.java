package com.gpadilla.mycar.dtos.contacto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactosDetalleDto {
    private ContactoTelefonicoDetailDto telefono;
    private ContactoCorreoDetailDto correo;
}
