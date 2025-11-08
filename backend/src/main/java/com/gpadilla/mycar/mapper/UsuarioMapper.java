package com.gpadilla.mycar.mapper;

import com.gpadilla.mycar.dtos.usuario.UsuarioDetailDto;
import com.gpadilla.mycar.entity.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    UsuarioDetailDto toDto(Usuario usuario);
}
