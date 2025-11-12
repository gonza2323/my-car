package com.gpadilla.mycar.repository;

import com.gpadilla.mycar.entity.Mensaje;
import com.gpadilla.mycar.enums.TipoMensaje;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MensajeRepository extends JpaRepository<Mensaje, Long> {

    List<Mensaje> findAllByEliminadoFalse();

    List<Mensaje> findAllByTipoAndEliminadoFalse(TipoMensaje tipo);

    Optional<Mensaje> findByIdAndEliminadoFalse(Long id);
}
