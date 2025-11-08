package com.gpadilla.mycar.mapper.geo;

import com.gpadilla.mycar.dtos.geo.localidad.LocalidadCreateOrUpdateDto;
import com.gpadilla.mycar.dtos.geo.localidad.LocalidadViewDto;
import com.gpadilla.mycar.entity.geo.Localidad;
import com.gpadilla.mycar.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface LocalidadMapper extends BaseMapper<
        Localidad,
        LocalidadViewDto,
        LocalidadViewDto,
        LocalidadCreateOrUpdateDto,
        LocalidadCreateOrUpdateDto> {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "eliminado", ignore = true)
    @Mapping(target = "departamento", ignore = true)
    Localidad toEntity(LocalidadCreateOrUpdateDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "eliminado", ignore = true)
    @Mapping(target = "departamento", ignore = true)
    void updateEntity(LocalidadCreateOrUpdateDto dto, @MappingTarget Localidad entity);

    @Mapping(target = "departamentoId", source = "departamento.id")
    @Mapping(target = "departamentoNombre", source = "departamento.nombre")
    @Mapping(target = "provinciaId", source = "departamento.provincia.id")
    @Mapping(target = "provinciaNombre", source = "departamento.provincia.nombre")
    @Mapping(target = "paisId", source = "departamento.provincia.pais.id")
    @Mapping(target = "paisNombre", source = "departamento.provincia.pais.nombre")
    LocalidadViewDto toDto(Localidad entity);;

    @Mapping(target = "departamentoId", source = "departamento.id")
    @Mapping(target = "departamentoNombre", source = "departamento.nombre")
    @Mapping(target = "provinciaId", source = "departamento.provincia.id")
    @Mapping(target = "provinciaNombre", source = "departamento.provincia.nombre")
    @Mapping(target = "paisId", source = "departamento.provincia.pais.id")
    @Mapping(target = "paisNombre", source = "departamento.provincia.pais.nombre")
    LocalidadViewDto toSummaryDto(Localidad entity);
}
