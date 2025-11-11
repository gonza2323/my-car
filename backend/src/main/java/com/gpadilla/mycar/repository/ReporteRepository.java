package com.gpadilla.mycar.repository;


import com.gpadilla.mycar.dtos.reportes.ReporteRecaudacionDto;
import com.gpadilla.mycar.entity.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReporteRepository extends JpaRepository<Factura, Long> {

    @Query("""
        SELECT new com.gpadilla.mycar.dtos.reportes.ReporteRecaudacionDto(
            ca.modelo,
            CAST(f.fechaFactura AS string),
            f.totalPagado
        )
        FROM Factura f
        JOIN f.detalles d
        JOIN d.alquiler alq
        JOIN alq.auto a
        JOIN a.caracteristicasAuto ca
        WHERE f.fechaFactura BETWEEN :fechaInicio AND :fechaFin
        ORDER BY f.fechaFactura ASC
    """)
    List<ReporteRecaudacionDto> findByFechas(String fechaInicio, String fechaFin);
}

