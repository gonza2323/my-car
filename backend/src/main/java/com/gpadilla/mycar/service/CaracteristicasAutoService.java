package com.gpadilla.mycar.service;

import com.gpadilla.mycar.dtos.caracteristicasAuto.*;
import com.gpadilla.mycar.dtos.imagen.ImagenCreateDto;
import com.gpadilla.mycar.entity.CaracteristicasAuto;
import com.gpadilla.mycar.entity.Imagen;
import com.gpadilla.mycar.enums.TipoImagen;
import com.gpadilla.mycar.error.BusinessException;
import com.gpadilla.mycar.mapper.CaracteristicasAutoMapper;
import com.gpadilla.mycar.repository.CaracteristicasAutoRepository;
import com.gpadilla.mycar.repository.CostoAutoRepository;
import com.gpadilla.mycar.repository.ImagenRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class CaracteristicasAutoService extends BaseService<
        CaracteristicasAuto,
        Long,
        CaracteristicasAutoRepository,
        CaracteristicasAutoDetailDto,
        CaracteristicasAutoSummaryDto,
        CaracteristicasAutoCreateDto,
        CaracteristicasAutoUpdateDto,
        CaracteristicasAutoMapper> {

    private final CostoAutoRepository costoAutoRepository;
    private final ImagenService imagenService;
    private final ImagenRepository imagenRepository;

    public CaracteristicasAutoService(CaracteristicasAutoRepository repository,
                                      CaracteristicasAutoMapper mapper,
                                      CostoAutoRepository costoAutoRepository,
                                      ImagenService imagenService, ImagenRepository imagenRepository) {
        super("CaracteristicasAuto", repository, mapper);
        this.costoAutoRepository = costoAutoRepository;
        this.imagenService = imagenService;
        this.imagenRepository = imagenRepository;
    }

    @Override
    protected void preCreate(CaracteristicasAutoCreateDto dto, CaracteristicasAuto entity) {
        entity.setCantTotalAutos(0);
    }

    @Override
    protected void postCreate(CaracteristicasAutoCreateDto dto, CaracteristicasAuto entity) {
        ImagenCreateDto imagenDto = dto.getImagen();

        if (imagenDto == null)
            return;

        Imagen imagen = Imagen.builder()
                .nombre(imagenDto.getNombre())
                .mime(imagenDto.getMime())
                .tipoImagen(TipoImagen.VEHICULO)
                .contenido(imagenDto.getContenido())
                .caracteristicasAuto(entity).build();

        imagenRepository.save(imagen);
    }

    @Override
    protected void validateCreate(CaracteristicasAutoCreateDto dto) {
        repository.findByMarcaAndModeloAndAnioAndEliminadoFalse(dto.getMarca(), dto.getModelo(), dto.getAnio())
                .ifPresent(c -> {
                    throw new BusinessException("Ya existe un modelo '" + dto.getModelo() + "' de la marca '" +
                            dto.getMarca() + "' y año " + dto.getAnio());
                });
    }

    @Transactional(readOnly = true)
    public Page<CaracteristicasAutoDisponible> encontrarModelosDisponiblesParaAlquiler(
            Pageable pageable,
            LocalDate fechaDesde,
            LocalDate fechaHasta) {
        Long cantDias = ChronoUnit.DAYS.between(fechaDesde, fechaHasta) + 1;
        return repository.encontrarModelosDisponiblesParaAlquiler(pageable, fechaDesde, fechaHasta, cantDias);
    }

    @Transactional(readOnly = true)
    public CaracteristicasAutoDisponible consultarModeloDisponible(Long modeloId, LocalDate fechaDesde, LocalDate fechaHasta) {
        // todo no verifica si está disponible, solo devuelve los datos, en teoría el front nunca llega mal acá, pero bueno está mal
        CaracteristicasAuto modelo = this.find(modeloId);

        Double costoPorDia = costoAutoRepository.buscarCostoDeModeloEnFecha(modeloId, fechaDesde)
                .orElseThrow(() -> new BusinessException("El modelo no tiene precio en esa fecha")).getCostoTotal();
        Long cantidadDias = ChronoUnit.DAYS.between(fechaDesde, fechaHasta) + 1;

        return CaracteristicasAutoDisponible.builder()
                .id(modeloId)
                .marca(modelo.getMarca())
                .modelo(modelo.getModelo())
                .anio(modelo.getAnio())
                .cantidadAsientos(modelo.getCantidadAsientos())
                .cantidadPuertas(modelo.getCantidadPuertas())
                .precioPorDia(costoPorDia)
                .precioTotal(costoPorDia * cantidadDias)
                .build();
    }
}

