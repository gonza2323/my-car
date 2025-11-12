package com.gpadilla.mycar.repository;


import com.gpadilla.mycar.dtos.reportes.ReporteRecaudacionDto;
import com.gpadilla.mycar.dtos.reportes.ReporteVehiculosDto;
import com.gpadilla.mycar.entity.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReporteRepository extends JpaRepository<Factura, Long> {

    @Query("""
    SELECT new com.gpadilla.mycar.dtos.reportes.ReporteVehiculosDto(
        CONCAT(c.marca, ' ', c.modelo),
        v.patente,
        CONCAT(cli.nombre, ' ', cli.apellido),
        a.fechaDesde,
        a.fechaHasta,
        DATEDIFF(a.fechaHasta, a.fechaDesde) + 1,
        a.monto
    )
    FROM Alquiler a
    JOIN a.auto v
    JOIN v.caracteristicasAuto c
    JOIN a.cliente cli
    WHERE a.fechaHasta >= :fechaInicio AND a.fechaDesde <= :fechaFin
    ORDER BY a.fechaDesde ASC
""")
    List<ReporteVehiculosDto> findVehiculosAlquiladosPorFechas(
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin
    );


    @Query(value = """
    SELECT
      CONCAT(c.marca, ' ', c.modelo) AS modelo,
      SUM(CASE
            WHEN a.fecha_desde >= :fechaInicio AND a.fecha_hasta <= :fechaFin
            THEN 1 ELSE 0 END) AS cantidad_cerrados,

      SUM(CASE
            WHEN a.fecha_desde >= :fechaInicio AND a.fecha_hasta <= :fechaFin
            THEN a.monto ELSE 0 END) AS total_cerrados,

      SUM(CASE
            WHEN a.fecha_hasta >= :fechaInicio AND a.fecha_desde <= :fechaFin
                 AND NOT (a.fecha_desde >= :fechaInicio AND a.fecha_hasta <= :fechaFin)
            THEN (DATEDIFF(LEAST(a.fecha_hasta, :fechaFin), GREATEST(a.fecha_desde, :fechaInicio)) + 1)
                 * a.costo_por_dia
            ELSE 0 END) AS total_abiertos

    FROM alquiler a
    JOIN auto v ON a.auto_id = v.id
    JOIN caracteristicas_auto c ON v.caracteristicas_auto_id = c.id
    WHERE a.fecha_hasta >= :fechaInicio AND a.fecha_desde <= :fechaFin
    GROUP BY CONCAT(c.marca, ' ', c.modelo)
""", nativeQuery = true)
    List<Object[]> findByFechas(
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin
    );


}

