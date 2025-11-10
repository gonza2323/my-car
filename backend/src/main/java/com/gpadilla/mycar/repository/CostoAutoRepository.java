package com.gpadilla.mycar.repository;

import com.gpadilla.mycar.entity.CostoAuto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

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
}
