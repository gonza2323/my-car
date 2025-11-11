package com.gpadilla.mycar.repository;

import com.gpadilla.mycar.entity.Auto;
import com.gpadilla.mycar.enums.EstadoAuto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AutoRepository extends BaseRepository<Auto, Long> {

    Optional<Auto> findByPatenteAndEliminadoFalse(String patente);

    List<Auto> findAllByEstadoAutoAndEliminadoFalse(EstadoAuto estadoAuto);

    List<Auto> findAllByCaracteristicasAutoIdAndEliminadoFalse(Long caracteristicasAutoId);


    @Query("""
SELECT v FROM Auto v
    WHERE v.caracteristicasAuto.id = :modeloId
      AND v.eliminado = false
      AND NOT EXISTS (
           SELECT al
           FROM Alquiler al
           WHERE al.auto = v
             AND al.eliminado = false
             AND :fechaDesde < al.fechaHasta
             AND :fechaHasta > al.fechaDesde
       )
""")
    List<Auto> encontrarVehiculosDisponiblesParaAlquiler(
            Long modeloId,
            LocalDate fechaDesde,
            LocalDate fechaHasta);
}

