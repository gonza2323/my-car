package com.gpadilla.mycar.repository;


import com.gpadilla.mycar.dtos.alquiler.AlquilerDetalleDto;
import com.gpadilla.mycar.dtos.alquiler.AlquilerSummaryDto;
import com.gpadilla.mycar.entity.Alquiler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AlquilerRepository extends JpaRepository<Alquiler, Long> {

    Optional<Alquiler> findByIdAndEliminadoFalse(Long id);

    Page<AlquilerSummaryDto> findAllByEliminadoFalse(Pageable pageable);

    @Query("""
SELECT new com.gpadilla.mycar.dtos.alquiler.AlquilerDetalleDto(
         al.id, al.estado, al.fechaDesde, al.fechaHasta, al.costoPorDia, al.monto,
         p.porcentajeDescuento, al.monto * p.porcentajeDescuento, f.totalPagado,
         new com.gpadilla.mycar.dtos.auto.AutoDetailDto(
            v.id,
            v.patente,
            v.estadoAuto,
            m.id,
            m.marca,
            m.modelo,
            m.cantidadPuertas,
            m.cantidadAsientos,
            m.anio,
            m.cantTotalAutos
         ),
         new com.gpadilla.mycar.dtos.cliente.ClienteSummaryDto(
            c.id,
            c.nombre,
            c.apellido,
            c.tipoDocumento,
            c.numeroDocumento,
            c.nacionalidad.nombre,
            c.usuario.email
         )
     )
     FROM Alquiler al
     LEFT JOIN al.cliente c
     LEFT JOIN al.auto v
     LEFT JOIN v.caracteristicasAuto m
     LEFT JOIN DetalleFactura df ON df.alquiler = al
     LEFT JOIN df.factura f
     LEFT JOIN df.promocion p
     WHERE c.usuario.id = :userId
       AND al.eliminado = false
""")
    Page<AlquilerDetalleDto> listarAlquileresDeUsuario(Pageable pageable, Long userId);
}
