package com.gpadilla.mycar.service;

import com.gpadilla.mycar.dtos.costoAuto.CostoAutoCreateDto;
import com.gpadilla.mycar.dtos.costoAuto.CostoAutoDto;
import com.gpadilla.mycar.dtos.costoAuto.CostoAutoUpdateDto;
import com.gpadilla.mycar.entity.CostoAuto;
import com.gpadilla.mycar.error.BusinessException;
import com.gpadilla.mycar.mapper.CostoAutoMapper;
import com.gpadilla.mycar.repository.CostoAutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;

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

    public CostoAutoService(CostoAutoRepository repository, CostoAutoMapper mapper) {
        super("CostoAuto", repository, mapper);
    }

    @Override
    protected void validateCreate(CostoAutoCreateDto dto) {
        if (dto.getFechaDesde().after(dto.getFechaHasta())) {
            throw new BusinessException("La fecha 'desde' no puede ser posterior a la fecha 'hasta'.");
        }
    }

    @Override
    protected void preCreate(CostoAutoCreateDto dto, CostoAuto entity) {
        // Por ejemplo, verificar solapamiento de costos para la misma característica
        Date desde = dto.getFechaDesde();
        Date hasta = dto.getFechaHasta();
        var conflictos = repository.findAllByFechaDesdeLessThanEqualAndFechaHastaGreaterThanEqualAndEliminadoFalse(desde, hasta);
        if (!conflictos.isEmpty()) {
            throw new BusinessException("Ya existe un costo definido en el rango de fechas indicado.");
        }
    }
}

