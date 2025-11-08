package com.gpadilla.mycar.repository;

import com.gpadilla.mycar.entity.Auto;
import com.gpadilla.mycar.enums.EstadoAuto;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AutoRepository extends BaseRepository<Auto, Long> {

    Optional<Auto> findByPatenteAndEliminadoFalse(String patente);

    List<Auto> findAllByEstadoAutoAndEliminadoFalse(EstadoAuto estadoAuto);

    List<Auto> findAllByCaracteristicasAutoIdAndEliminadoFalse(Long caracteristicasAutoId);
}

