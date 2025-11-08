package com.gpadilla.mycar.repository.geo;

import com.gpadilla.mycar.entity.Provincia;
import com.gpadilla.mycar.repository.BaseRepository;

public interface ProvinciaRepository extends BaseRepository<Provincia, Long> {
    boolean existsByNombreAndPaisIdAndEliminadoFalse(String nombre, Long paisId);
    boolean existsByNombreAndPaisIdAndIdNotAndEliminadoFalse(String nombre, Long paisId, Long id);
}
