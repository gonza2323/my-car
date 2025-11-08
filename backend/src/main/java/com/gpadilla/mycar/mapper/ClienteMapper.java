package com.gpadilla.mycar.mapper;

import com.gpadilla.mycar.dtos.cliente.ClienteCompleteProfileDto;
import com.gpadilla.mycar.dtos.cliente.ClienteCreateDto;
import com.gpadilla.mycar.dtos.cliente.ClienteCreateRequestDto;
import com.gpadilla.mycar.entity.Cliente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClienteMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "eliminado", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "direccion", ignore = true)
    @Mapping(target = "nacionalidad", ignore = true)
    Cliente toEntity(ClienteCreateDto dto);

    ClienteCreateDto toDto(ClienteCreateRequestDto request);
    ClienteCreateDto toDto(ClienteCompleteProfileDto request);
}
