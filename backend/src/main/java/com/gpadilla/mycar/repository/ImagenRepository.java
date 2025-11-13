package com.gpadilla.mycar.repository;

import com.gpadilla.mycar.entity.Imagen;
import com.gpadilla.mycar.enums.TipoImagen;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImagenRepository extends BaseRepository<Imagen, Long> {

    List<Imagen> findAllByCaracteristicasAutoIdAndEliminadoFalse(Long caracteristicasAutoId);

    List<Imagen> findAllByTipoImagenAndEliminadoFalse(TipoImagen tipo);

    Optional<Imagen> findByCaracteristicasAutoIdAndEliminadoFalse(Long modeloId);
}
