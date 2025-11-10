package com.gpadilla.mycar.mapper.geo;

import com.gpadilla.mycar.dtos.geo.nacionalidad.NacionalidadCreateOrUpdateDto;
import com.gpadilla.mycar.dtos.geo.nacionalidad.NacionalidadViewDto;
import com.gpadilla.mycar.entity.geo.Nacionalidad;
import com.gpadilla.mycar.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface NacionalidadMapper extends BaseMapper<
        Nacionalidad,
        NacionalidadViewDto,
        NacionalidadViewDto,
        NacionalidadCreateOrUpdateDto,
        NacionalidadCreateOrUpdateDto> {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "eliminado", ignore = true)
    Nacionalidad toEntity(NacionalidadCreateOrUpdateDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "eliminado", ignore = true)
    void updateEntity(NacionalidadCreateOrUpdateDto dto, @MappingTarget Nacionalidad entity);

    NacionalidadViewDto toDto(Nacionalidad entity);;

    NacionalidadViewDto toSummaryDto(Nacionalidad entity);
}
