package com.gpadilla.mycar.mapper;

import com.gpadilla.mycar.dtos.imagen.ImagenCreateDto;
import com.gpadilla.mycar.dtos.imagen.ImagenDetailDto;
import com.gpadilla.mycar.entity.Imagen;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.Collections;
import java.util.List;

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
    @Named("toDto")
    ImagenDetailDto toDto(Imagen entity);

    @Override
    ImagenDetailDto toSummaryDto(Imagen entity);

    @Named("toDtoList")
    default List<ImagenDetailDto> toDtoList(List<Imagen> entities) {
        return entities == null
                ? Collections.emptyList()
                : entities.stream().map(this::toDto).toList();
    }
}


