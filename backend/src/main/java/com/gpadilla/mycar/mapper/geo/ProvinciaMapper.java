package com.gpadilla.mycar.mapper.geo;

import com.gpadilla.mycar.dtos.geo.provincia.ProvinciaCreateOrUpdateDto;
import com.gpadilla.mycar.dtos.geo.provincia.ProvinciaViewDto;
import com.gpadilla.mycar.entity.Provincia;
import com.gpadilla.mycar.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProvinciaMapper extends BaseMapper<
        Provincia,
        ProvinciaViewDto,
        ProvinciaViewDto,
        ProvinciaCreateOrUpdateDto,
        ProvinciaCreateOrUpdateDto> {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "eliminado", ignore = true)
    @Mapping(target = "pais", ignore = true)
    Provincia toEntity(ProvinciaCreateOrUpdateDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "eliminado", ignore = true)
    @Mapping(target = "pais", ignore = true)
    void updateEntity(ProvinciaCreateOrUpdateDto dto, @MappingTarget Provincia entity);

    @Mapping(target = "paisId", source = "pais.id")
    @Mapping(target = "paisNombre", source = "pais.nombre")
    ProvinciaViewDto toDto(Provincia entity);;

    @Mapping(target = "paisId", source = "pais.id")
    @Mapping(target = "paisNombre", source = "pais.nombre")
    ProvinciaViewDto toSummaryDto(Provincia entity);
}
