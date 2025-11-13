package com.gpadilla.mycar.mapper;

import com.gpadilla.mycar.dtos.alquiler.AlquilerDto;
import com.gpadilla.mycar.entity.Alquiler;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AlquilerMapper extends BaseMapper<
        Alquiler,
        AlquilerDto,     // DetailDto
        AlquilerDto,     // SummaryDto (podés tener otro si querés resumir)
        AlquilerDto,     // CreateDto
        AlquilerDto      // UpdateDto
        > {
}
