package com.gpadilla.mycar.repository.geo;

import com.gpadilla.mycar.entity.Departamento;
import com.gpadilla.mycar.repository.BaseRepository;

public interface DepartamentoRepository extends BaseRepository<Departamento, Long> {
    boolean existsByNombreAndProvinciaIdAndEliminadoFalse(String nombre, Long provId);
    boolean existsByNombreAndProvinciaIdAndIdNotAndEliminadoFalse(String nombre, Long provId, Long id);
}
