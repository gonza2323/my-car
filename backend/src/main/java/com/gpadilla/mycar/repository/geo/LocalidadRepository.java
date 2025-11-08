package com.gpadilla.mycar.repository.geo;

import com.gpadilla.mycar.entity.Localidad;
import com.gpadilla.mycar.repository.BaseRepository;

public interface LocalidadRepository extends BaseRepository<Localidad, Long> {
    boolean existsByNombreAndDepartamentoIdAndEliminadoFalse(String nombre, Long deptId);
    boolean existsByNombreAndDepartamentoIdAndIdNotAndEliminadoFalse(String nombre, Long deptId, Long id);
}
