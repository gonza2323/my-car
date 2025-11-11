package com.gpadilla.mycar.repository;

import com.gpadilla.mycar.entity.Promocion;

import java.util.Optional;

public interface PromocionRepository extends BaseRepository<Promocion,Long> {

    Optional<Promocion> findByCodigoDescuentoAndEliminadoFalse(String codigo);

    boolean existsByCodigoDescuentoAndEliminadoFalse(String codigoDescuento);
}
