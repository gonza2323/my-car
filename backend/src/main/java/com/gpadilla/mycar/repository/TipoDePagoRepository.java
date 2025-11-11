package com.gpadilla.mycar.repository;

import com.gpadilla.mycar.entity.FormaDePago;
import com.gpadilla.mycar.enums.TipoDePago;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoDePagoRepository extends JpaRepository<FormaDePago,Long> {
    boolean existsByTipoDePago(TipoDePago tipoDePago);
}
