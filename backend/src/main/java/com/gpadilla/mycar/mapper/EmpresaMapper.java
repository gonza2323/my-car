package com.gpadilla.mycar.mapper;


import com.gpadilla.mycar.dtos.empresa.EmpresaCreateOrUpdateDto;
import com.gpadilla.mycar.dtos.empresa.EmpresaDetailDto;
import com.gpadilla.mycar.entity.ContactoCorreoElectronico;
import com.gpadilla.mycar.entity.ContactoTelefonico;
import com.gpadilla.mycar.entity.Empresa;
import com.gpadilla.mycar.enums.TipoTelefono;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmpresaMapper extends BaseMapper<
        Empresa,
        EmpresaDetailDto,
        EmpresaDetailDto,
        EmpresaCreateOrUpdateDto,
        EmpresaCreateOrUpdateDto> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "eliminado", ignore = true)
    @Mapping(target = "direccion", ignore = true) // la setea el service con direccionId
    @Mapping(target = "telefonoPrincipal", expression = "java(toContactoTel(dto.getTelefonoPrincipal()))")
    @Mapping(target = "emailPrincipal", expression = "java(toContactoEmail(dto.getEmailPrincipal()))")
    Empresa toEntity(EmpresaCreateOrUpdateDto dto);

    @Override
    @Mapping(target = "telefonoPrincipal", expression = "java(fromContactoTel(entity.getTelefonoPrincipal()))")
    @Mapping(target = "emailPrincipal", expression = "java(fromContactoEmail(entity.getEmailPrincipal()))")
    EmpresaDetailDto toDto(Empresa entity);

    @Override
    @Mapping(target = "direccion", ignore = true) // la resuelve el service
    @Mapping(target = "telefonoPrincipal", expression = "java(toContactoTel(dto.getTelefonoPrincipal()))")
    @Mapping(target = "emailPrincipal", expression = "java(toContactoEmail(dto.getEmailPrincipal()))")
    void updateEntity(EmpresaCreateOrUpdateDto dto, @MappingTarget Empresa entity);

    @Override
    EmpresaDetailDto toSummaryDto(Empresa entity);

    // ---- Helpers mínimos ----
    default ContactoTelefonico toContactoTel(String t) {
        if (t == null || t.isBlank()) return null;
        ContactoTelefonico c = new ContactoTelefonico();
        c.setTelefono(t);
        c.setTipoTelefono(TipoTelefono.CELULAR); // default razonable
        return c;
    }
    default String fromContactoTel(ContactoTelefonico c) {
        return (c != null) ? c.getTelefono() : null;
    }
    default ContactoCorreoElectronico toContactoEmail(String e) {
        if (e == null || e.isBlank()) return null;
        ContactoCorreoElectronico c = new ContactoCorreoElectronico();
        c.setEmail(e);
        return c;
    }
    default String fromContactoEmail(ContactoCorreoElectronico c) {
        return (c != null) ? c.getEmail() : null;
    }
}
