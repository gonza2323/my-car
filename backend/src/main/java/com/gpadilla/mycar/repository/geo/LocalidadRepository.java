package com.gpadilla.mycar.repository.geo;

import com.gpadilla.mycar.entity.geo.Localidad;
import com.gpadilla.mycar.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LocalidadRepository extends BaseRepository<Localidad, Long> {
    boolean existsByNombreAndDepartamentoIdAndEliminadoFalse(String nombre, Long deptId);
    boolean existsByNombreAndDepartamentoIdAndIdNotAndEliminadoFalse(String nombre, Long deptId, Long id);

    @Query("""
    SELECT l FROM Localidad l
    WHERE l.eliminado = false
      AND (:departamentoId IS NULL OR l.departamento.id = :departamentoId)
""")
    Page<Localidad> buscarPorDepartamentoId(
            Pageable pageable,
            @Param("departamentoId") Long departamentoId
    );
}
