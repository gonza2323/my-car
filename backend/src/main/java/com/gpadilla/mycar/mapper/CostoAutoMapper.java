package com.gpadilla.mycar.mapper;

import com.gpadilla.mycar.dtos.costoAuto.CostoAutoCreateDto;
import com.gpadilla.mycar.dtos.costoAuto.CostoAutoDto;
import com.gpadilla.mycar.dtos.costoAuto.CostoAutoUpdateDto;
import com.gpadilla.mycar.entity.CostoAuto;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CostoAutoMapper extends BaseMapper<
        CostoAuto,
        CostoAutoDto,           // DetailDto (podés usar un DetailDto más específico si querés)
        CostoAutoDto,           // SummaryDto
        CostoAutoCreateDto,
        CostoAutoUpdateDto
        > {

    @Override
    CostoAuto toEntity(CostoAutoCreateDto dto);

    @Override
    void updateEntity(CostoAutoUpdateDto dto, @MappingTarget CostoAuto entity);

    @Override
    @Named("toDto")
    CostoAutoDto toDto(CostoAuto entity);

    @Override
    @Named("toSummary")
    CostoAutoDto toSummaryDto(CostoAuto entity);
}


