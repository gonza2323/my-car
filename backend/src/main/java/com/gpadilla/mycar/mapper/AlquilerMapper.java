package com.gpadilla.mycar.mapper;

import com.gpadilla.mycar.dtos.alquiler.AlquilerDto;
import com.gpadilla.mycar.entity.Alquiler;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;


@Mapper(componentModel = "spring", uses = { AutoMapper.class })
public interface AlquilerMapper extends BaseMapper<
        Alquiler,
        AlquilerDto,
        AlquilerDto,    // Summary
        AlquilerDto,
        AlquilerDto
        > {

    // Usado por FacturaMapper
    @Override
    @Named("alquilerToDto")
    @Mapping(target = "auto", qualifiedByName = "autoToSummary")
    AlquilerDto toDto(Alquiler entity);

    // Usado cuando BaseMapper pide Summary
    @Override
    @Named("alquilerToSummary")
    @Mapping(target = "auto", qualifiedByName = "autoToSummary")
    AlquilerDto toSummaryDto(Alquiler entity);
}

//
//@Mapper(componentModel = "spring", uses = { AutoMapper.class })
//public interface AlquilerMapper extends BaseMapper<
//        Alquiler,
//        AlquilerDto,
//        AlquilerDto,
//        AlquilerDto,
//        AlquilerDto
//        > {
//
//    @Named("alquilerToDto")
//    @Mapping(target = "auto", qualifiedByName = "autoToSummary")
//    AlquilerDto toDto(Alquiler entity);

//    @Override
//    @Mapping(target = "auto", qualifiedByName = "toSummary")
//    @Named("toDto")
//    AlquilerDto toDto(Alquiler entity);
//
//    @Override
//    @Mapping(target = "auto", qualifiedByName = "toSummary")
//    @Named("toSummaryDto")
//    AlquilerDto toSummaryDto(Alquiler entity);
//}

