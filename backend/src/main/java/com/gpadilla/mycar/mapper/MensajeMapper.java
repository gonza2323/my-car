package com.gpadilla.mycar.mapper;

import com.gpadilla.mycar.dtos.mensaje.MensajeDTO;
import com.gpadilla.mycar.entity.Mensaje;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MensajeMapper {

    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "eliminado", ignore = true)
    @Mapping(target = "fechaEnvio", ignore = true)
    @Mapping(target = "cuerpoHtml", source = "html")
    @Mapping(target = "email", source = "emailDestino")
    @Mapping(target = "nombre", source = "nombreDestino")
    @Mapping(target = "adjuntoNombre", ignore = true)
    Mensaje toEntity(MensajeDTO dto);

    @Mapping(target = "usuarioId", source = "usuario.id")
    @Mapping(target = "html", source = "cuerpoHtml")
    @Mapping(target = "emailDestino", source = "email")
    @Mapping(target = "nombreDestino", source = "nombre")
    MensajeDTO toDto(Mensaje mensaje);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "nombre", source = "nombreDestino")
    @Mapping(target = "email", source = "emailDestino")
    @Mapping(target = "asunto")
    @Mapping(target = "cuerpoHtml", source = "html")
    @Mapping(target = "tipo")
    void updateEntityFromDto(MensajeDTO dto, @MappingTarget Mensaje mensaje);
}
