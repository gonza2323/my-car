package com.gpadilla.mycar.mapper;

import com.gpadilla.mycar.dtos.promocion.PromocionCreateDto;
import com.gpadilla.mycar.dtos.promocion.PromocionViewDto;
import com.gpadilla.mycar.entity.Promocion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PromocionMapper extends BaseMapper<
        Promocion,
        PromocionViewDto,
        PromocionViewDto,
        PromocionCreateDto,
        PromocionCreateDto> {

    Promocion toEntity(PromocionCreateDto dto);

    void updateEntity(PromocionCreateDto dto, @MappingTarget Promocion entity);

    PromocionViewDto toDto(Promocion entity);;

    PromocionViewDto toSummaryDto(Promocion entity);
}
