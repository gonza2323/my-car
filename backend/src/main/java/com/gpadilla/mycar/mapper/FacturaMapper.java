package com.gpadilla.mycar.mapper;

import com.gpadilla.mycar.dtos.factura.FacturaDto;
import com.gpadilla.mycar.entity.Factura;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface FacturaMapper extends BaseMapper<
        Factura,
        FacturaDto,
        FacturaDto,
        FacturaDto,
        FacturaDto
        > {

    @Override
    Factura toEntity(FacturaDto dto);

    @Override
    void updateEntity(FacturaDto dto, @MappingTarget Factura entity);

    @Override
    FacturaDto toDto(Factura entity);

    @Override
    FacturaDto toSummaryDto(Factura entity);
}
