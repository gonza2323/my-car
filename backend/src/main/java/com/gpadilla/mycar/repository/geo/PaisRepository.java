package com.gpadilla.mycar.repository.geo;

import com.gpadilla.mycar.entity.geo.Pais;
import com.gpadilla.mycar.repository.BaseRepository;

public interface PaisRepository extends BaseRepository<Pais, Long> {
    boolean existsByNombreAndEliminadoFalse(String nombre);
    boolean existsByNombreAndIdNotAndEliminadoFalse(String nombre, Long id);
}
