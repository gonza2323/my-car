package com.gpadilla.mycar.repository.geo;

import com.gpadilla.mycar.entity.geo.Provincia;
import com.gpadilla.mycar.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProvinciaRepository extends BaseRepository<Provincia, Long> {
    boolean existsByNombreAndPaisIdAndEliminadoFalse(String nombre, Long paisId);
    boolean existsByNombreAndPaisIdAndIdNotAndEliminadoFalse(String nombre, Long paisId, Long id);

    @Query("""
    SELECT p FROM Provincia p
    WHERE p.eliminado = false
      AND (:paisId IS NULL OR p.pais.id = :paisId)
""")
    Page<Provincia> buscarPorPaisId(
            Pageable pageable,
            @Param("paisId") Long paisId
    );
}
