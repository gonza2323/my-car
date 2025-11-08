package com.gpadilla.mycar.repository;

import com.gpadilla.mycar.entity.CaracteristicasAuto;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CaracteristicasAutoRepository
        extends BaseRepository<CaracteristicasAuto, Long> {

    Optional<CaracteristicasAuto> findByMarcaAndModeloAndEliminadoFalse(String marca, String modelo);

    List<CaracteristicasAuto> findAllByMarcaAndEliminadoFalse(String marca);
}
