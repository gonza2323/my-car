package com.gpadilla.mycar.service.geo;

import com.gpadilla.mycar.dtos.geo.localidad.LocalidadCreateOrUpdateDto;
import com.gpadilla.mycar.dtos.geo.localidad.LocalidadViewDto;
import com.gpadilla.mycar.entity.geo.Departamento;
import com.gpadilla.mycar.entity.geo.Localidad;
import com.gpadilla.mycar.error.BusinessException;
import com.gpadilla.mycar.mapper.geo.LocalidadMapper;
import com.gpadilla.mycar.repository.geo.LocalidadRepository;
import com.gpadilla.mycar.service.BaseService;
import org.springframework.stereotype.Service;

@Service
public class LocalidadService extends BaseService<
        Localidad,
        Long,
        LocalidadRepository,
        LocalidadViewDto,
        LocalidadViewDto,
        LocalidadCreateOrUpdateDto,
        LocalidadCreateOrUpdateDto,
        LocalidadMapper> {

    private final DepartamentoService departamentoService;

    public LocalidadService(LocalidadRepository repository, LocalidadMapper mapper, DepartamentoService departamentoService) {
        super("Localidad", repository, mapper);
        this.departamentoService = departamentoService;
    }

    @Override
    protected void preCreate(LocalidadCreateOrUpdateDto dto, Localidad localidad) {
        Departamento departamento = departamentoService.find(dto.getDepartamentoId());
        localidad.setDepartamento(departamento);
    }

    @Override
    protected void preUpdate(LocalidadCreateOrUpdateDto dto, Localidad localidad) {
        Departamento departamento = departamentoService.find(dto.getDepartamentoId());
        localidad.setDepartamento(departamento);
    }

    @Override
    protected void validateCreate(LocalidadCreateOrUpdateDto dto) {
        if (repository.existsByNombreAndDepartamentoIdAndEliminadoFalse(dto.getNombre(), dto.getDepartamentoId()))
            throw new BusinessException("Ya existe una localidad con ese nombre en ese departamento");
    }

    @Override
    protected void validateUpdate(Long id, LocalidadCreateOrUpdateDto dto) {
        if (repository.existsByNombreAndDepartamentoIdAndIdNotAndEliminadoFalse(dto.getNombre(), dto.getDepartamentoId(), id))
            throw new BusinessException("Ya existe una localidad con ese nombre en ese departamento");
    }
}
