package com.gpadilla.mycar.repository.geo;

import com.gpadilla.mycar.entity.geo.Departamento;
import com.gpadilla.mycar.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DepartamentoRepository extends BaseRepository<Departamento, Long> {
    boolean existsByNombreAndProvinciaIdAndEliminadoFalse(String nombre, Long provId);
    boolean existsByNombreAndProvinciaIdAndIdNotAndEliminadoFalse(String nombre, Long provId, Long id);

    @Query("""
    SELECT d FROM Departamento d
    WHERE d.eliminado = false
      AND (:provinciaId IS NULL OR d.provincia.id = :provinciaId)
""")
    Page<Departamento> buscarPorProvinciaId(
            Pageable pageable,
            @Param("provinciaId") Long provinciaId
    );
}
