package com.gpadilla.mycar.service.geo;

import com.gpadilla.mycar.dtos.geo.direccion.DireccionCreateOrUpdateDto;
import com.gpadilla.mycar.dtos.geo.direccion.DireccionViewDto;
import com.gpadilla.mycar.entity.geo.Direccion;
import com.gpadilla.mycar.entity.geo.Localidad;
import com.gpadilla.mycar.mapper.geo.DireccionMapper;
import com.gpadilla.mycar.repository.geo.DireccionRepository;
import com.gpadilla.mycar.service.BaseService;
import org.springframework.stereotype.Service;

@Service
public class DireccionService extends BaseService<
        Direccion,
        Long,
        DireccionRepository,
        DireccionViewDto,
        DireccionViewDto,
        DireccionCreateOrUpdateDto,
        DireccionCreateOrUpdateDto,
        DireccionMapper> {

    private final LocalidadService localidadService;

    public DireccionService(DireccionRepository repository, DireccionMapper mapper, LocalidadService localidadService) {
        super("Dirección", repository, mapper);
        this.localidadService = localidadService;
    }

    @Override
    protected void preCreate(DireccionCreateOrUpdateDto dto, Direccion direccion) {
        Localidad localidad = localidadService.find(dto.getLocalidadId());
        direccion.setLocalidad(localidad);
    }

    @Override
    protected void preUpdate(DireccionCreateOrUpdateDto dto, Direccion direccion) {
        Localidad localidad = localidadService.find(dto.getLocalidadId());
        direccion.setLocalidad(localidad);
    }
}
