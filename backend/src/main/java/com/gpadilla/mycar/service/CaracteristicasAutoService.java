package com.gpadilla.mycar.service;

import com.gpadilla.mycar.dtos.caracteristicasAuto.*;
import com.gpadilla.mycar.entity.CaracteristicasAuto;
import com.gpadilla.mycar.error.BusinessException;
import com.gpadilla.mycar.mapper.CaracteristicasAutoMapper;
import com.gpadilla.mycar.repository.CaracteristicasAutoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
public class CaracteristicasAutoService extends BaseService<
        CaracteristicasAuto,
        Long,
        CaracteristicasAutoRepository,
        CaracteristicasAutoDetailDto,
        CaracteristicasAutoSummaryDto,
        CaracteristicasAutoCreateDto,
        CaracteristicasAutoUpdateDto,
        CaracteristicasAutoMapper> {

    public CaracteristicasAutoService(CaracteristicasAutoRepository repository, CaracteristicasAutoMapper mapper) {
        super("CaracteristicasAuto", repository, mapper);
    }

    @Override
    protected void preCreate(CaracteristicasAutoCreateDto dto, CaracteristicasAuto entity) {
        entity.setCantTotalAutos(0);

        // todo foto
    }

    @Override
    protected void validateCreate(CaracteristicasAutoCreateDto dto) {
        repository.findByMarcaAndModeloAndAnioAndEliminadoFalse(dto.getMarca(), dto.getModelo(), dto.getAnio())
                .ifPresent(c -> {
                    throw new BusinessException("Ya existe un modelo '" + dto.getModelo() + "' de la marca '" + dto.getMarca() + "'");
                });
    }

    @Transactional(readOnly = true)
    public Page<CaracteristicasAutoDisponible> encontrarModelosDisponiblesParaAlquiler(
            Pageable pageable,
            LocalDate fechaDesde,
            LocalDate fechaHasta) {
        return repository.encontrarModelosDisponiblesParaAlquiler(pageable, fechaDesde, fechaHasta);
    }
}

