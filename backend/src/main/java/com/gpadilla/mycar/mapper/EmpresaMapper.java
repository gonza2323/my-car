package com.gpadilla.mycar.mapper;


import com.gpadilla.mycar.dtos.empresa.EmpresaCreateOrUpdateDto;
import com.gpadilla.mycar.dtos.empresa.EmpresaDetailDto;
import com.gpadilla.mycar.entity.Empresa;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmpresaMapper extends BaseMapper<
        Empresa,
        EmpresaDetailDto,           // Detail
        EmpresaDetailDto,           // Summary (reutilizado)
        EmpresaCreateOrUpdateDto,   // Create
        EmpresaCreateOrUpdateDto    // Update
        > {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "eliminado", ignore = true)
    @Mapping(target = "direccion", ignore = true) // la setea el service con direccionId
    Empresa toEntity(EmpresaCreateOrUpdateDto dto);

    @Override
    @Mapping(target = "direccion", source = "direccion") // mapea tal cual para el Detail
    EmpresaDetailDto toDto(Empresa entity);

    @Override
    @Mapping(target = "direccion", ignore = true) // la resuelve el service
    void updateEntity(EmpresaCreateOrUpdateDto dto, @MappingTarget Empresa entity);

    @Override
    EmpresaDetailDto toSummaryDto(Empresa entity);
}
