package com.gpadilla.mycar.mapper;

import com.gpadilla.mycar.dtos.factura.FacturaDto;
import com.gpadilla.mycar.entity.Factura;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = { AlquilerMapper.class })
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

    @Mapping(target = "detalles[].alquiler", qualifiedByName = "alquilerToDto")
    FacturaDto toDto(Factura entity);

//    @Override
//    @Mapping(
//            target = "detalles[].alquiler",
//            qualifiedByName = "alquilerToDto"
//    )
//    FacturaDto toDto(Factura entity);

//    @Override
//    @Mapping(
//            target = "detalles[].alquiler",
//            qualifiedByName = "alquilerToSummary"
//    )
//    FacturaDto toSummaryDto(Factura entity);
}
