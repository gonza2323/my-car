package com.gpadilla.mycar.mapper;

import com.gpadilla.mycar.dtos.auto.AutoCreateDto;
import com.gpadilla.mycar.dtos.auto.AutoDetailDto;
import com.gpadilla.mycar.dtos.auto.AutoSummaryDto;
import com.gpadilla.mycar.dtos.auto.AutoUpdateDto;
import com.gpadilla.mycar.entity.Auto;
import org.mapstruct.*;
@Mapper(componentModel = "spring")
public interface AutoMapper extends BaseMapper<
        Auto,
        AutoDetailDto,
        AutoSummaryDto,
        AutoCreateDto,
        AutoUpdateDto
        > {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "eliminado", ignore = true)
    @Mapping(target = "estadoAuto", ignore = true)
    @Mapping(target = "caracteristicasAuto", ignore = true)
    Auto toEntity(AutoCreateDto dto);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "eliminado", ignore = true)
    @Mapping(target = "caracteristicasAuto", ignore = true)
    void updateEntity(AutoUpdateDto dto, @MappingTarget Auto entity);

    // ===============================
    //    ESTE ES EL IMPORTANTE
    // ===============================
    @Override
    @Named("autoToSummary")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "patente", source = "patente")
    @Mapping(target = "estadoAuto", source = "estadoAuto")

    @Mapping(target = "caracteristicaAutoId", source = "caracteristicasAuto.id")
    @Mapping(target = "marca", source = "caracteristicasAuto.marca")
    @Mapping(target = "modelo", source = "caracteristicasAuto.modelo")
    @Mapping(target = "anio", source = "caracteristicasAuto.anio")

    // estos campos NO existen en summary → ignorar
   // @Mapping(target = "cantidadPuertas", ignore = true)
//    @Mapping(target = "cantidadAsientos", ignore = true)
//    @Mapping(target = "cantTotalAutos", ignore = true)

    AutoSummaryDto toSummaryDto(Auto entity);


    @Override
    @Named("toDto")
    @Mapping(target = "caracteristicaAutoId", source = "caracteristicasAuto.id")
    @Mapping(target = "marca", source = "caracteristicasAuto.marca")
    @Mapping(target = "modelo", source = "caracteristicasAuto.modelo")
    @Mapping(target = "anio", source = "caracteristicasAuto.anio")
    @Mapping(target = "cantidadPuertas", source = "caracteristicasAuto.cantidadPuertas")
    @Mapping(target = "cantidadAsientos", source = "caracteristicasAuto.cantidadAsientos")
    @Mapping(target = "cantTotalAutos", source = "caracteristicasAuto.cantTotalAutos")
    AutoDetailDto toDto(Auto entity);
}


