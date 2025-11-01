package com.gpadilla.mycar.mapper;

import com.gpadilla.mycar.dtos.usuario.UsuarioCreateDto;
import com.gpadilla.mycar.dtos.usuario.UsuarioDetailDto;
import com.gpadilla.mycar.dtos.usuario.UsuarioSummaryDto;
import com.gpadilla.mycar.dtos.usuario.UsuarioUpdateDto;
import com.gpadilla.mycar.entity.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UsuarioMapper extends BaseMapper<Usuario, UsuarioDetailDto, UsuarioSummaryDto, UsuarioCreateDto, UsuarioUpdateDto> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "eliminado", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "providerId", ignore = true)
    @Mapping(target = "rol", ignore = true)
    Usuario toEntity(UsuarioCreateDto Dto);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "eliminado", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "providerId", ignore = true)
    @Mapping(target = "rol", ignore = true)
    void updateEntity(UsuarioUpdateDto dto, @MappingTarget Usuario usuario);

    @Override
    UsuarioDetailDto toDto(Usuario usuario);

    @Override
    UsuarioSummaryDto toSummaryDto(Usuario usuario);
}
