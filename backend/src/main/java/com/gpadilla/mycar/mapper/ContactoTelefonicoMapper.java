package com.gpadilla.mycar.mapper;

import com.gpadilla.mycar.dtos.contacto.ContactoCreateOrUpdateDto;
import com.gpadilla.mycar.dtos.contacto.ContactoTelefonicoDetailDto;
import com.gpadilla.mycar.entity.ContactoTelefonico;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ContactoTelefonicoMapper extends BaseMapper<
        ContactoTelefonico,
        ContactoTelefonicoDetailDto,  // Detail
        ContactoTelefonicoDetailDto,  // (reusado)
        ContactoCreateOrUpdateDto,    // Create
        ContactoCreateOrUpdateDto     // Update
        > {

    @Override
    @Mappings({
            @Mapping(target = "empresaId", source = "empresa.id"),
            @Mapping(target = "usuarioId", source = "usuario.id")
    })
    ContactoTelefonicoDetailDto toDto(ContactoTelefonico entity);

    @Override
    @Mappings({
            @Mapping(target = "empresa", ignore = true),
            @Mapping(target = "usuario", ignore = true)
    })
    ContactoTelefonico toEntity(ContactoCreateOrUpdateDto dto);

    @Override
    @Mappings({
            @Mapping(target = "empresa", ignore = true),
            @Mapping(target = "usuario", ignore = true)
    })
    void updateEntity(ContactoCreateOrUpdateDto dto, @MappingTarget ContactoTelefonico entity);
}
