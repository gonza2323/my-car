package com.gpadilla.mycar.service;

import com.gpadilla.mycar.dtos.imagen.ImagenCreateDto;
import com.gpadilla.mycar.dtos.imagen.ImagenDetailDto;
import com.gpadilla.mycar.entity.Imagen;
import com.gpadilla.mycar.error.BusinessException;
import com.gpadilla.mycar.mapper.ImagenMapper;
import com.gpadilla.mycar.repository.ImagenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional
public class ImagenService extends BaseService<
        Imagen,
        Long,
        ImagenRepository,
        ImagenDetailDto,
        ImagenDetailDto,
        ImagenCreateDto,
        ImagenDetailDto,
        ImagenMapper> {

    public ImagenService(ImagenRepository repository, ImagenMapper mapper) {
        super("Imagen", repository, mapper);
    }

    @Override
    protected void validateCreate(ImagenCreateDto dto) {
        if (dto.getContenido() == null || dto.getContenido().length == 0) {
            throw new BusinessException("El contenido de la imagen no puede estar vacío.");
        }
        var permitidos = Set.of("image/png", "image/jpeg", "image/webp");
        if (dto.getMime() == null || !permitidos.contains(dto.getMime()))
            throw new BusinessException("Tipo MIME no permitido: " + dto.getMime());
    }

    @Transactional(readOnly = true)
    public ImagenDetailDto findByVehicleId(Long vehicleId) {
        Imagen imagen = repository.findByCaracteristicasAutoIdAndEliminadoFalse(vehicleId)
                .orElseThrow();

        return ImagenDetailDto.builder()
                .id(imagen.getId())
                .mime(imagen.getMime())
                .tipo(imagen.getTipoImagen())
                .nombre(imagen.getNombre())
                .contenido(imagen.getContenido()).build();
    }
}

