package com.gpadilla.mycar.dtos.reportes;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteReporteDto {
    private Long id;
    private String nombre;
    private String apellido;
    private String correo;
    private Long cantidadAlquileres;
}

