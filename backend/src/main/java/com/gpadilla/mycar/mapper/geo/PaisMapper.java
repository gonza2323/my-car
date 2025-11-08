package com.gpadilla.mycar.mapper.geo;

import com.gpadilla.mycar.dtos.geo.pais.PaisCreateOrUpdateDto;
import com.gpadilla.mycar.dtos.geo.pais.PaisViewDto;
import com.gpadilla.mycar.entity.Pais;
import com.gpadilla.mycar.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PaisMapper extends BaseMapper<
        Pais,
        PaisViewDto,
        PaisViewDto,
        PaisCreateOrUpdateDto,
        PaisCreateOrUpdateDto> {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "eliminado", ignore = true)
    Pais toEntity(PaisCreateOrUpdateDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "eliminado", ignore = true)
    void updateEntity(PaisCreateOrUpdateDto dto, @MappingTarget Pais entity);

    PaisViewDto toDto(Pais entity);;

    PaisViewDto toSummaryDto(Pais entity);
}
