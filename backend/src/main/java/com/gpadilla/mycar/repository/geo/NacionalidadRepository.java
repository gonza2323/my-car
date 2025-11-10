package com.gpadilla.mycar.repository.geo;


import com.gpadilla.mycar.entity.geo.Nacionalidad;
import com.gpadilla.mycar.repository.BaseRepository;

public interface NacionalidadRepository extends BaseRepository<Nacionalidad, Long> {
    boolean existsByNombreAndEliminadoFalse(String nombre);
    boolean existsByNombreAndIdNotAndEliminadoFalse(String nombre, Long id);
}
