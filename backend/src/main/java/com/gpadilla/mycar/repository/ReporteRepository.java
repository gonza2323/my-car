package com.gpadilla.mycar.repository;

import com.gpadilla.mycar.dtos.reportes.ReporteRecaudacionDto;
import com.gpadilla.mycar.dtos.reportes.ClienteReporteDto;
import com.gpadilla.mycar.entity.BaseEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReporteRepository extends BaseRepository<BaseEntity<Long>, Long> {

    // 🔹 Reporte de recaudación filtrado por fechas
    @Query("""
        SELECT new com.gpadilla.mycar.dtos.reportes.ReporteRecaudacionDto(
            ca.modelo,
            CAST(a.fechaAlquiler AS string),
            SUM(a.montoTotal)
        )
        FROM Alquiler a
        JOIN a.auto au
        JOIN au.caracteristicasAuto ca
        WHERE a.eliminado = false
        AND a.fechaAlquiler BETWEEN :fechaInicio AND :fechaFin
        GROUP BY ca.modelo, a.fechaAlquiler
        ORDER BY a.fechaAlquiler DESC
    """)
    List<ReporteRecaudacionDto> findByFechas(
            @Param("fechaInicio") String fechaInicio,
            @Param("fechaFin") String fechaFin
    );

    // 🔹 (opcional) Reporte de clientes con cantidad de alquileres
    @Query("""
        SELECT new com.gpadilla.mycar.dtos.reportes.ClienteReporteDto(
            c.id,
            c.nombre,
            c.apellido,
            c.usuario.email,
            COUNT(a.id)
        )
        FROM Cliente c
        LEFT JOIN Alquiler a ON a.cliente.id = c.id
        WHERE c.eliminado = false
        GROUP BY c.id, c.nombre, c.apellido, c.usuario.email
        ORDER BY COUNT(a.id) DESC
    """)
    List<ClienteReporteDto> findClientesConAlquileres();
}
