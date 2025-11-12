package com.gpadilla.mycar.repository;


import com.gpadilla.mycar.dtos.cliente.ClienteSummaryDto;
import com.gpadilla.mycar.entity.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    @Query("""
SELECT DISTINCT new com.gpadilla.mycar.dtos.cliente.ClienteSummaryDto(
    c.id, c.nombre, c.apellido, c.tipoDocumento, c.numeroDocumento, c.nacionalidad.nombre, c.usuario.email
) FROM Cliente c
""")
    Page<ClienteSummaryDto> buscarResumenClientes(Pageable pageable);

    Optional<Cliente> findByIdAndEliminadoFalse(Long id);
}
