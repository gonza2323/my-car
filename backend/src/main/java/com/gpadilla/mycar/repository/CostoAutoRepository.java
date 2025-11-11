package com.gpadilla.mycar.repository;

import com.gpadilla.mycar.entity.CostoAuto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CostoAutoRepository extends BaseRepository<CostoAuto, Long> {

    List<CostoAuto> findAllByCaracteristicasAutoIdAndEliminadoFalse(Long caracteristicasAutoId);

    @Query("""
    SELECT c FROM CostoAuto c
    WHERE c.caracteristicasAuto.id = :modeloId
      AND c.eliminado = false
      AND c.fechaDesde <= :hasta
      AND c.fechaHasta >= :desde
""")
    List<CostoAuto> findConflictingCostos(
            @Param("modeloId") Long modeloId,
            @Param("desde") LocalDate desde,
            @Param("hasta") LocalDate hasta
    );

    @Query("""
SELECT c FROM CostoAuto c
    WHERE c.caracteristicasAuto.id = :modeloId
        AND c.fechaDesde <= :fecha
        AND c.fechaHasta >= :fecha
""")
    Optional<CostoAuto> buscarCostoDeModeloEnFecha(@Param("modeloId") Long modeloId, @Param("fecha") LocalDate fecha);
}
