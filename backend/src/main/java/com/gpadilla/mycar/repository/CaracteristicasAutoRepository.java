package com.gpadilla.mycar.repository;

import com.gpadilla.mycar.dtos.caracteristicasAuto.CaracteristicasAutoDisponible;
import com.gpadilla.mycar.entity.CaracteristicasAuto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CaracteristicasAutoRepository
        extends BaseRepository<CaracteristicasAuto, Long> {

    Optional<CaracteristicasAuto> findByMarcaAndModeloAndAnioAndEliminadoFalse(String marca, String modelo, Integer anio);

    List<CaracteristicasAuto> findAllByMarcaAndEliminadoFalse(String marca);

    @Query("""
    SELECT new com.gpadilla.mycar.dtos.caracteristicasAuto.CaracteristicasAutoDisponible(
         c.id, c.marca, c.modelo, c.anio, c.cantidadPuertas, c.cantidadAsientos,
         (SELECT co.costoTotal
          FROM CostoAuto co
          WHERE co.caracteristicasAuto = c
            AND co.eliminado = false
            AND co.fechaDesde <= :startDate
            AND co.fechaHasta >= :startDate
          ORDER BY co.fechaDesde DESC
          LIMIT 1)
     )
     FROM CaracteristicasAuto c
     JOIN c.autos a
     WHERE c.eliminado = false
       AND a.eliminado = false
       AND NOT EXISTS (
           SELECT al
           FROM Alquiler al
           WHERE al.auto = a
             AND al.eliminado = false
             AND :startDate < al.fechaHasta
             AND :endDate > al.fechaDesde
       )
     GROUP BY c.id, c.marca, c.modelo, c.anio, c.cantidadPuertas, c.cantidadAsientos
""")
    Page<CaracteristicasAutoDisponible> encontrarModelosDisponiblesParaAlquiler(
            Pageable pageable,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
