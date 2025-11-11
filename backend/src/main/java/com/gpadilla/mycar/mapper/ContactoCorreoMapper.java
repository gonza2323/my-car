package com.gpadilla.mycar.mapper;

import com.gpadilla.mycar.dtos.contacto.ContactoCorreoDetailDto;
import com.gpadilla.mycar.dtos.contacto.ContactoCreateOrUpdateDto;
import com.gpadilla.mycar.entity.ContactoCorreoElectronico;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ContactoCorreoMapper extends BaseMapper<
        ContactoCorreoElectronico,
        ContactoCorreoDetailDto,
        ContactoCorreoDetailDto,
        ContactoCreateOrUpdateDto,
        ContactoCreateOrUpdateDto
        >{

    @Override
    @Mappings({
            @Mapping(target = "empresaId", source = "empresa.id"),
            @Mapping(target = "usuarioId", source = "usuario.id")
    })
    ContactoCorreoDetailDto toDto(ContactoCorreoElectronico entity);

    @Override
    @Mappings({
            @Mapping(target = "empresa", ignore = true),
            @Mapping(target = "usuario", ignore = true)
    })
    ContactoCorreoElectronico toEntity(ContactoCreateOrUpdateDto dto);

    @Override
    @Mappings({
            @Mapping(target = "empresa", ignore = true),
            @Mapping(target = "usuario", ignore = true)
    })
    void updateEntity(ContactoCreateOrUpdateDto dto, @MappingTarget ContactoCorreoElectronico entity);

}
