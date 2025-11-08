package com.gpadilla.mycar.mapper;

import com.gpadilla.mycar.dtos.empleado.EmpleadoCreateDto;
import com.gpadilla.mycar.dtos.empleado.EmpleadoCreateRequestDto;
import com.gpadilla.mycar.entity.Empleado;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmpleadoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "eliminado", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "direccion", ignore = true)
    Empleado toEntity(EmpleadoCreateDto dto);

    EmpleadoCreateDto toDto(EmpleadoCreateRequestDto request);
}
