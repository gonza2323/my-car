package com.gpadilla.mycar.mapper;

import com.gpadilla.mycar.dtos.imagen.ImagenCreateDto;
import com.gpadilla.mycar.dtos.imagen.ImagenDetailDto;
import com.gpadilla.mycar.entity.Imagen;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ImagenMapper extends BaseMapper<
        Imagen,
        ImagenDetailDto,       // DetailDto
        ImagenDetailDto,       // SummaryDto
        ImagenCreateDto,
        ImagenDetailDto        // Update no aplica, así que se usa el mismo para compatibilidad
        > {

    @Override
    Imagen toEntity(ImagenCreateDto dto);

    @Override
    void updateEntity(ImagenDetailDto dto, @MappingTarget Imagen entity);

    @Override
    ImagenDetailDto toDto(Imagen entity);

    @Override
    ImagenDetailDto toSummaryDto(Imagen entity);
}


