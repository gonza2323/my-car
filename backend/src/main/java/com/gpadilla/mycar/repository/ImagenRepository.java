package com.gpadilla.mycar.repository;

import com.gpadilla.mycar.entity.Imagen;
import com.gpadilla.mycar.entity.TipoImagen;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ImagenRepository extends BaseRepository<Imagen, Long> {

    List<Imagen> findAllByCaracteristicasAutoIdAndEliminadoFalse(Long caracteristicasAutoId);

    List<Imagen> findAllByTipoImagenAndEliminadoFalse(TipoImagen tipo);
}
