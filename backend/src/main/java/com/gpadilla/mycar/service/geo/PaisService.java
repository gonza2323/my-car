package com.gpadilla.mycar.service.geo;

import com.gpadilla.mycar.dtos.geo.pais.PaisCreateOrUpdateDto;
import com.gpadilla.mycar.dtos.geo.pais.PaisViewDto;
import com.gpadilla.mycar.entity.geo.Pais;
import com.gpadilla.mycar.error.BusinessException;
import com.gpadilla.mycar.mapper.geo.PaisMapper;
import com.gpadilla.mycar.repository.geo.PaisRepository;
import com.gpadilla.mycar.service.BaseService;
import org.springframework.stereotype.Service;

@Service
public class PaisService extends BaseService<
        Pais,
        Long,
        PaisRepository,
        PaisViewDto,
        PaisViewDto,
        PaisCreateOrUpdateDto,
        PaisCreateOrUpdateDto,
        PaisMapper> {

    public PaisService(PaisRepository repository, PaisMapper mapper) {
        super("País", repository, mapper);
    }

    @Override
    protected void validateCreate(PaisCreateOrUpdateDto dto) {
        if (repository.existsByNombreAndEliminadoFalse(dto.getNombre()))
            throw new BusinessException("Ya existe un país con ese nombre");
    }

    @Override
    protected void validateUpdate(Long id, PaisCreateOrUpdateDto dto) {
        if (repository.existsByNombreAndIdNotAndEliminadoFalse(dto.getNombre(), id))
            throw new BusinessException("Ya existe un país con ese nombre");
    }
}
