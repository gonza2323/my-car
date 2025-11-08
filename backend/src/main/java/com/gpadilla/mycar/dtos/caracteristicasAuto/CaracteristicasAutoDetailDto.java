package com.gpadilla.mycar.dtos.caracteristicasAuto;

import com.gpadilla.mycar.dtos.IdentifiableDto;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CaracteristicasAutoDetailDto extends IdentifiableDto<Long> {
    private String marca;
    private String modelo;
    private int cantidadPuertas;
    private int cantidadAsientos;
    private int anio;
    private int cantTotalVehiculos;
    private int cantidadAlquilados;

    private List<ImagenDto> imagenes;
    private List<CostoAutoDto> costos;
}
