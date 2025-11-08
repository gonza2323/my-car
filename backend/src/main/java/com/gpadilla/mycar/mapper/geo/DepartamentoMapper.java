package com.gpadilla.mycar.mapper.geo;

import com.gpadilla.mycar.dtos.geo.departamento.DepartamentoCreateOrUpdateDto;
import com.gpadilla.mycar.dtos.geo.departamento.DepartamentoViewDto;
import com.gpadilla.mycar.entity.Departamento;
import com.gpadilla.mycar.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DepartamentoMapper extends BaseMapper<
        Departamento,
        DepartamentoViewDto,
        DepartamentoViewDto,
        DepartamentoCreateOrUpdateDto,
        DepartamentoCreateOrUpdateDto> {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "eliminado", ignore = true)
    @Mapping(target = "provincia", ignore = true)
    Departamento toEntity(DepartamentoCreateOrUpdateDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "eliminado", ignore = true)
    @Mapping(target = "provincia", ignore = true)
    void updateEntity(DepartamentoCreateOrUpdateDto dto, @MappingTarget Departamento entity);

    @Mapping(target = "provinciaId", source = "provincia.id")
    @Mapping(target = "provinciaNombre", source = "provincia.nombre")
    @Mapping(target = "paisId", source = "provincia.pais.id")
    @Mapping(target = "paisNombre", source = "provincia.pais.nombre")
    DepartamentoViewDto toDto(Departamento entity);;

    @Mapping(target = "provinciaId", source = "provincia.id")
    @Mapping(target = "provinciaNombre", source = "provincia.nombre")
    @Mapping(target = "paisId", source = "provincia.pais.id")
    @Mapping(target = "paisNombre", source = "provincia.pais.nombre")
    DepartamentoViewDto toSummaryDto(Departamento entity);
}
