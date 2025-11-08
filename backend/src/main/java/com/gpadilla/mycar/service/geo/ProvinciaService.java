package com.gpadilla.mycar.service.geo;

import com.gpadilla.mycar.dtos.geo.provincia.ProvinciaCreateOrUpdateDto;
import com.gpadilla.mycar.dtos.geo.provincia.ProvinciaViewDto;
import com.gpadilla.mycar.entity.geo.Pais;
import com.gpadilla.mycar.entity.geo.Provincia;
import com.gpadilla.mycar.error.BusinessException;
import com.gpadilla.mycar.mapper.geo.ProvinciaMapper;
import com.gpadilla.mycar.repository.geo.ProvinciaRepository;
import com.gpadilla.mycar.service.BaseService;
import org.springframework.stereotype.Service;

@Service
public class ProvinciaService extends BaseService<
        Provincia,
        Long,
        ProvinciaRepository,
        ProvinciaViewDto,
        ProvinciaViewDto,
        ProvinciaCreateOrUpdateDto,
        ProvinciaCreateOrUpdateDto,
        ProvinciaMapper> {

    private final PaisService paisService;

    public ProvinciaService(ProvinciaRepository repository, ProvinciaMapper mapper, PaisService paisService) {
        super("Provincia", repository, mapper);
        this.paisService = paisService;
    }
    
    @Override
    protected void preCreate(ProvinciaCreateOrUpdateDto dto, Provincia provincia) {
        Pais pais = paisService.find(dto.getPaisId());
        provincia.setPais(pais);
    }

    @Override
    protected void preUpdate(ProvinciaCreateOrUpdateDto dto, Provincia provincia) {
        Pais pais = paisService.find(dto.getPaisId());
        provincia.setPais(pais);
    }

    @Override
    protected void validateCreate(ProvinciaCreateOrUpdateDto dto) {
        if (repository.existsByNombreAndPaisIdAndEliminadoFalse(dto.getNombre(), dto.getPaisId()))
            throw new BusinessException("Ya existe una provincia con ese nombre en ese país");
    }

    @Override
    protected void validateUpdate(Long id, ProvinciaCreateOrUpdateDto dto) {
        if (repository.existsByNombreAndPaisIdAndIdNotAndEliminadoFalse(dto.getNombre(), dto.getPaisId(), id))
            throw new BusinessException("Ya existe una provincia con ese nombre en ese país");
    }
}
