package com.gpadilla.mycar.service;

import com.gpadilla.mycar.dtos.costoAuto.CostoAutoCreateDto;
import com.gpadilla.mycar.dtos.costoAuto.CostoAutoDto;
import com.gpadilla.mycar.dtos.costoAuto.CostoAutoUpdateDto;
import com.gpadilla.mycar.entity.CaracteristicasAuto;
import com.gpadilla.mycar.entity.CostoAuto;
import com.gpadilla.mycar.error.BusinessException;
import com.gpadilla.mycar.mapper.CostoAutoMapper;
import com.gpadilla.mycar.repository.CostoAutoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
public class CostoAutoService extends BaseService<
        CostoAuto,
        Long,
        CostoAutoRepository,
        CostoAutoDto,
        CostoAutoDto,
        CostoAutoCreateDto,
        CostoAutoUpdateDto,
        CostoAutoMapper> {

    private final CaracteristicasAutoService caracteristicasAutoService;

    public CostoAutoService(CostoAutoRepository repository, CostoAutoMapper mapper, CaracteristicasAutoService caracteristicasAutoService) {
        super("CostoAuto", repository, mapper);
        this.caracteristicasAutoService = caracteristicasAutoService;
    }

    @Override
    protected void validateCreate(CostoAutoCreateDto dto) {
        if (dto.getFechaDesde().isAfter(dto.getFechaHasta())) {
            throw new BusinessException("La fecha 'desde' no puede ser posterior a la fecha 'hasta'.");
        }

        var conflictos = repository.findConflictingCostos(
                dto.getCaracteristicasAutoId(),
                dto.getFechaDesde(),
                dto.getFechaHasta()
        );

        if (!conflictos.isEmpty()) {
            throw new BusinessException("Ya existe un costo definido en el rango de fechas indicado para este modelo.");
        }
    }

    @Override
    protected void preCreate(CostoAutoCreateDto dto, CostoAuto entity) {
        CaracteristicasAuto modelo = caracteristicasAutoService.find(dto.getCaracteristicasAutoId());
        entity.setCaracteristicasAuto(modelo);
    }

    public CostoAuto buscarCostoDeModeloEnFecha(Long modeloId, LocalDate fecha) {
        return repository.buscarCostoDeModeloEnFecha(modeloId, fecha)
                .orElseThrow(() -> new BusinessException("El modelo no tiene precio en esa fecha"));
    }

    public Page<CostoAutoDto> findDtosDeModelo(Pageable pageable, Long modeloId) {
        return repository.findByCaracteristicasAutoId(pageable, modeloId);
    }
}

