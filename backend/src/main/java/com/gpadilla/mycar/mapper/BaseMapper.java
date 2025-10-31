package com.gpadilla.mycar.mapper;

import org.mapstruct.MappingTarget;

public interface BaseMapper<E, DetailDto, SummaryDto, CreateDto, UpdateDto> {
    E toEntity(CreateDto Dto);
    void updateEntity(UpdateDto dto, @MappingTarget E entity);

    DetailDto toDto(E entity);
    SummaryDto toSummaryDto(E entity);
}
