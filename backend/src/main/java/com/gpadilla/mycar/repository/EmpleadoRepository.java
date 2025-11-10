package com.gpadilla.mycar.repository;


import com.gpadilla.mycar.dtos.empleado.EmpleadoSummaryDto;
import com.gpadilla.mycar.entity.Empleado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {

    Page<EmpleadoSummaryDto> findAllByEliminadoFalse(Pageable pageable);

    Optional<Empleado> findByIdAndEliminadoFalse(Long id);
}
