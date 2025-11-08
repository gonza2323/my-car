package com.gpadilla.mycar.mapper.geo;

import com.gpadilla.mycar.dtos.geo.direccion.DireccionCreateOrUpdateDto;
import com.gpadilla.mycar.dtos.geo.direccion.DireccionViewDto;
import com.gpadilla.mycar.entity.Direccion;
import com.gpadilla.mycar.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DireccionMapper extends BaseMapper<
        Direccion,
        DireccionViewDto,
        DireccionViewDto,
        DireccionCreateOrUpdateDto,
        DireccionCreateOrUpdateDto> {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "eliminado", ignore = true)
    @Mapping(target = "localidad", ignore = true)
    Direccion toEntity(DireccionCreateOrUpdateDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "eliminado", ignore = true)
    @Mapping(target = "localidad", ignore = true)
    void updateEntity(DireccionCreateOrUpdateDto dto, @MappingTarget Direccion entity);

    @Mapping(target = "localidadId", source = "localidad.id")
    @Mapping(target = "localidadNombre", source = "localidad.nombre")
    @Mapping(target = "departamentoId", source = "localidad.departamento.id")
    @Mapping(target = "departamentoNombre", source = "localidad.departamento.nombre")
    @Mapping(target = "provinciaId", source = "localidad.departamento.provincia.id")
    @Mapping(target = "provinciaNombre", source = "localidad.departamento.provincia.nombre")
    @Mapping(target = "paisId", source = "localidad.departamento.provincia.pais.id")
    @Mapping(target = "paisNombre", source = "localidad.departamento.provincia.pais.nombre")
    DireccionViewDto toDto(Direccion entity);;

    @Mapping(target = "localidadId", source = "localidad.id")
    @Mapping(target = "localidadNombre", source = "localidad.nombre")
    @Mapping(target = "departamentoId", source = "localidad.departamento.id")
    @Mapping(target = "departamentoNombre", source = "localidad.departamento.nombre")
    @Mapping(target = "provinciaId", source = "localidad.departamento.provincia.id")
    @Mapping(target = "provinciaNombre", source = "localidad.departamento.provincia.nombre")
    @Mapping(target = "paisId", source = "localidad.departamento.provincia.pais.id")
    @Mapping(target = "paisNombre", source = "localidad.departamento.provincia.pais.nombre")
    DireccionViewDto toSummaryDto(Direccion entity);
}
