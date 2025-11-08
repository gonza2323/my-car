package com.gpadilla.mycar.service;

import com.gpadilla.mycar.dtos.auto.AutoCreateDto;
import com.gpadilla.mycar.dtos.auto.AutoDetailDto;
import com.gpadilla.mycar.dtos.auto.AutoSummaryDto;
import com.gpadilla.mycar.dtos.auto.AutoUpdateDto;
import com.gpadilla.mycar.entity.Auto;
import com.gpadilla.mycar.entity.EstadoAuto;
import com.gpadilla.mycar.error.BusinessException;
import com.gpadilla.mycar.mapper.AutoMapper;
import com.gpadilla.mycar.repository.AutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AutoService extends BaseService<
        Auto,
        Long,
        AutoRepository,
        AutoDetailDto,
        AutoSummaryDto,
        AutoCreateDto,
        AutoUpdateDto,
        AutoMapper> {

    public AutoService(AutoRepository repository, AutoMapper mapper) {
        super("Auto", repository, mapper);
    }

    // Ejemplo de validación específica:
    @Override
    protected void validateCreate(AutoCreateDto dto) {
        repository.findByPatenteAndEliminadoFalse(dto.getPatente())
                .ifPresent(a -> {
                    throw new BusinessException("Ya existe un auto con la patente " + dto.getPatente());
                });
    }

    @Override
    protected void preDelete(Auto entity) {
        if (entity.getEstadoAuto() == EstadoAuto.ALQUILADO) {
            throw new BusinessException("No se puede eliminar un auto actualmente alquilado");
        }
    }
}

