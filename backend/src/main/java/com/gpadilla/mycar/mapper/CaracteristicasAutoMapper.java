package com.gpadilla.mycar.mapper;

import com.gpadilla.mycar.dtos.caracteristicasAuto.CaracteristicasAutoCreateDto;
import com.gpadilla.mycar.dtos.caracteristicasAuto.CaracteristicasAutoDetailDto;
import com.gpadilla.mycar.dtos.caracteristicasAuto.CaracteristicasAutoSummaryDto;
import com.gpadilla.mycar.dtos.caracteristicasAuto.CaracteristicasAutoUpdateDto;
import com.gpadilla.mycar.entity.CaracteristicasAuto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = { ImagenMapper.class, CostoAutoMapper.class } // permite mapear colecciones internas
)
public interface CaracteristicasAutoMapper extends BaseMapper<
        CaracteristicasAuto,
        CaracteristicasAutoDetailDto,
        CaracteristicasAutoSummaryDto,
        CaracteristicasAutoCreateDto,
        CaracteristicasAutoUpdateDto
        > {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "eliminado", ignore = true)
    @Mapping(target = "cantTotalAutos", ignore = true)
    CaracteristicasAuto toEntity(CaracteristicasAutoCreateDto dto);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "eliminado", ignore = true)
    void updateEntity(CaracteristicasAutoUpdateDto dto, @MappingTarget CaracteristicasAuto entity);

    @Override
    @Mapping(target = "imagenes", qualifiedByName = "toDto")
    @Mapping(target = "costos", qualifiedByName = "toDto")
    @Mapping(target = "cantTotalAutos", source = "cantTotalAutos")
    @Mapping(target = "cantidadAlquilados", ignore = true)
    CaracteristicasAutoDetailDto toDto(CaracteristicasAuto entity);

    @Override
    CaracteristicasAutoSummaryDto toSummaryDto(CaracteristicasAuto entity);
}


