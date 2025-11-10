package com.gpadilla.mycar.repository;


import com.gpadilla.mycar.dtos.cliente.ClienteSummaryDto;
import com.gpadilla.mycar.entity.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Page<ClienteSummaryDto> findAllByEliminadoFalse(Pageable pageable);

    Optional<Cliente> findByIdAndEliminadoFalse(Long id);
}
