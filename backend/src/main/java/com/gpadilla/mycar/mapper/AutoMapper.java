package com.gpadilla.mycar.mapper;

import com.gpadilla.mycar.dtos.auto.AutoCreateDto;
import com.gpadilla.mycar.dtos.auto.AutoDetailDto;
import com.gpadilla.mycar.dtos.auto.AutoSummaryDto;
import com.gpadilla.mycar.dtos.auto.AutoUpdateDto;
import com.gpadilla.mycar.entity.Auto;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AutoMapper extends BaseMapper<
        Auto,
        AutoDetailDto,
        AutoSummaryDto,
        AutoCreateDto,
        AutoUpdateDto
        > {

    @Override
    Auto toEntity(AutoCreateDto dto);

    @Override
    void updateEntity(AutoUpdateDto dto, @MappingTarget Auto entity);

    @Override
    AutoDetailDto toDto(Auto entity);

    @Override
    AutoSummaryDto toSummaryDto(Auto entity);
}


