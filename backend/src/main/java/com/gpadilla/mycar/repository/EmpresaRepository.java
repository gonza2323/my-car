package com.gpadilla.mycar.repository;

import com.gpadilla.mycar.entity.Empresa;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EmpresaRepository extends BaseRepository<Empresa, Long> {
    Optional<Empresa> findFirstByEliminadoFalseOrderByIdAsc();
}
