package com.gpadilla.mycar.repository;

import com.gpadilla.mycar.entity.CostoAuto;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;

@Repository
public interface CostoAutoRepository extends BaseRepository<CostoAuto, Long> {

    List<CostoAuto> findAllByCaracteristicasAutoIdAndEliminadoFalse(Long caracteristicasAutoId);

    List<CostoAuto> findAllByFechaDesdeLessThanEqualAndFechaHastaGreaterThanEqualAndEliminadoFalse(
            Date desde, Date hasta);
}
