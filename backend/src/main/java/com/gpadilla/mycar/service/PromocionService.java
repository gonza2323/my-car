package com.gpadilla.mycar.service;

import com.gpadilla.mycar.dtos.promocion.PromocionCreateDto;
import com.gpadilla.mycar.dtos.promocion.PromocionViewDto;
import com.gpadilla.mycar.entity.Promocion;
import com.gpadilla.mycar.error.BusinessException;
import com.gpadilla.mycar.mapper.PromocionMapper;
import com.gpadilla.mycar.repository.PromocionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class PromocionService extends BaseService<
        Promocion,
        Long,
        PromocionRepository,
        PromocionViewDto,
        PromocionViewDto,
        PromocionCreateDto,
        PromocionCreateDto,
        PromocionMapper> {

    public PromocionService(PromocionRepository repository, PromocionMapper mapper) {
        super("Promoción", repository, mapper);
    }

    @Override
    protected void preCreate(PromocionCreateDto dto, Promocion entity) {
        if (repository.existsByCodigoDescuentoAndEliminadoFalse(dto.getCodigoDescuento()))
            throw new BusinessException("Ya existe una promoción con ese código");
    }

    @Transactional(readOnly = true)
    public Promocion validarCodigoPromocion(String codigo) {
        Promocion promocion = repository.findByCodigoDescuentoAndEliminadoFalse(codigo)
                .orElseThrow(() -> new BusinessException("Código de descuento invalido"));

        LocalDate hoy = LocalDate.now();

        if (hoy.isBefore(promocion.getFechaInicio()))
            throw new BusinessException("La promoción aún no entró en vigencia");

        if (hoy.isAfter(promocion.getFechaFin()))
            throw new BusinessException("La promoción ya no es válida");

        return promocion;
    }
}
