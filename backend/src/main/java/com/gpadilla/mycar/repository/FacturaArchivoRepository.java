package com.gpadilla.mycar.repository;

import com.gpadilla.mycar.entity.FacturaArchivo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FacturaArchivoRepository extends BaseRepository<FacturaArchivo, Long> {
    Optional<FacturaArchivo> findByFacturaId(Long facturaId);

    @Query("""
        SELECT fa FROM FacturaArchivo fa
        WHERE fa.factura.id IN (
            SELECT f.id FROM Factura f
            JOIN f.detalles d
            WHERE d.alquiler.id = :alquilerId
        )
    """)
    Optional<FacturaArchivo> findByFactura_AlquilerId(@Param("alquilerId") Long alquilerId);
}


