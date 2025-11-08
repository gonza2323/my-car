package com.gpadilla.mycar.service.geo;

import com.gpadilla.mycar.dtos.geo.departamento.DepartamentoCreateOrUpdateDto;
import com.gpadilla.mycar.dtos.geo.departamento.DepartamentoViewDto;
import com.gpadilla.mycar.entity.Provincia;
import com.gpadilla.mycar.entity.Departamento;
import com.gpadilla.mycar.error.BusinessException;
import com.gpadilla.mycar.mapper.geo.DepartamentoMapper;
import com.gpadilla.mycar.repository.geo.DepartamentoRepository;
import com.gpadilla.mycar.service.BaseService;
import org.springframework.stereotype.Service;

@Service
public class DepartamentoService extends BaseService<
        Departamento,
        Long,
        DepartamentoRepository,
        DepartamentoViewDto,
        DepartamentoViewDto,
        DepartamentoCreateOrUpdateDto,
        DepartamentoCreateOrUpdateDto,
        DepartamentoMapper> {

    private final ProvinciaService provinciaService;

    public DepartamentoService(DepartamentoRepository repository, DepartamentoMapper mapper, ProvinciaService provinciaService) {
        super("Departamento", repository, mapper);
        this.provinciaService = provinciaService;
    }
    
    @Override
    protected void preCreate(DepartamentoCreateOrUpdateDto dto, Departamento departamento) {
        Provincia provincia = provinciaService.find(dto.getProvinciaId());
        departamento.setProvincia(provincia);
    }

    @Override
    protected void preUpdate(DepartamentoCreateOrUpdateDto dto, Departamento departamento) {
        Provincia provincia = provinciaService.find(dto.getProvinciaId());
        departamento.setProvincia(provincia);
    }

    @Override
    protected void validateCreate(DepartamentoCreateOrUpdateDto dto) {
        if (repository.existsByNombreAndProvinciaIdAndEliminadoFalse(dto.getNombre(), dto.getProvinciaId()))
            throw new BusinessException("Ya existe un departamento con ese nombre en esa provincia");
    }

    @Override
    protected void validateUpdate(Long id, DepartamentoCreateOrUpdateDto dto) {
        if (repository.existsByNombreAndProvinciaIdAndIdNotAndEliminadoFalse(dto.getNombre(), dto.getProvinciaId(), id))
            throw new BusinessException("Ya existe un departamento con ese nombre en esa provincia");
    }
}
