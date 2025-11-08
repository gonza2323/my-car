package com.gpadilla.mycar.mapper;

import com.gpadilla.mycar.dtos.auto.AutoCreateDto;
import com.gpadilla.mycar.dtos.auto.AutoDetailDto;
import com.gpadilla.mycar.dtos.auto.AutoSummaryDto;
import com.gpadilla.mycar.dtos.auto.AutoUpdateDto;
import com.gpadilla.mycar.entity.Auto;

import com.gpadilla.mycar.entity.CaracteristicasAuto;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AutoMapper extends BaseMapper<
        Auto,
        AutoDetailDto,
        AutoSummaryDto,
        AutoCreateDto,
        AutoUpdateDto
        > {

    @Override
    @Mapping(target = "caracteristicasAuto", source = "caracteristicasAuto", qualifiedByName = "mapCaracteristica")
    Auto toEntity(AutoCreateDto dto);

    @Override

    void updateEntity(AutoUpdateDto dto, @MappingTarget Auto entity);

    @Override
    @Named("toDto")
    @Mapping(target = "caracteristicasAuto", source = "caracteristicasAuto", qualifiedByName = "mapCaracteristicaToId")
    AutoDetailDto toDto(Auto entity);

    @Override
    @Named("toSummary")
    AutoSummaryDto toSummaryDto(Auto entity);

    @Named("mapCaracteristica")
    default CaracteristicasAuto mapCaracteristica(Long id) {
        if (id == null) return null;
        CaracteristicasAuto c = new CaracteristicasAuto();
        c.setId(id);
        return c;
    }

    @Named("mapCaracteristicaToId")
    default Long mapCaracteristicaToId(CaracteristicasAuto entity) {
        return (entity != null) ? entity.getId() : null;
    }
}


