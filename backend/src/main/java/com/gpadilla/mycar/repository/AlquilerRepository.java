package com.gpadilla.mycar.repository;


import com.gpadilla.mycar.dtos.alquiler.AlquilerSummaryDto;
import com.gpadilla.mycar.entity.Alquiler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlquilerRepository extends JpaRepository<Alquiler, Long> {

    Optional<Alquiler> findByIdAndEliminadoFalse(Long id);

    Page<AlquilerSummaryDto> findAllByEliminadoFalse(Pageable pageable);
}
