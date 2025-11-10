package com.gpadilla.mycar.service.geo;

import com.gpadilla.mycar.dtos.geo.nacionalidad.NacionalidadCreateOrUpdateDto;
import com.gpadilla.mycar.dtos.geo.nacionalidad.NacionalidadViewDto;
import com.gpadilla.mycar.entity.geo.Nacionalidad;
import com.gpadilla.mycar.error.BusinessException;
import com.gpadilla.mycar.mapper.geo.NacionalidadMapper;
import com.gpadilla.mycar.repository.geo.NacionalidadRepository;
import com.gpadilla.mycar.service.BaseService;
import org.springframework.stereotype.Service;

@Service
public class NacionalidadService extends BaseService<
        Nacionalidad,
        Long,
        NacionalidadRepository,
        NacionalidadViewDto,
        NacionalidadViewDto,
        NacionalidadCreateOrUpdateDto,
        NacionalidadCreateOrUpdateDto,
        NacionalidadMapper> {

    public NacionalidadService(NacionalidadRepository repository, NacionalidadMapper mapper) {
        super("País", repository, mapper);
    }

    @Override
    protected void validateCreate(NacionalidadCreateOrUpdateDto dto) {
        if (repository.existsByNombreAndEliminadoFalse(dto.getNombre()))
            throw new BusinessException("Ya existe un país con ese nombre");
    }

    @Override
    protected void validateUpdate(Long id, NacionalidadCreateOrUpdateDto dto) {
        if (repository.existsByNombreAndIdNotAndEliminadoFalse(dto.getNombre(), id))
            throw new BusinessException("Ya existe un país con ese nombre");
    }
}
