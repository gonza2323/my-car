package com.gpadilla.mycar.dtos.mensaje;

import com.gpadilla.mycar.enums.TipoMensaje;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromocionDTO {

    private Long id;

    @NotNull
    private Double porcentajeDescuento;

    @NotBlank
    @Size(max = 100)
    private String codigoDescuento;

    @Size(max = 500)
    private String descripcion;

    @NotNull
    private LocalDate fechaInicio;

    @NotNull
    private LocalDate fechaFin;

    private TipoMensaje tipo = TipoMensaje.PROMOCION;
}
