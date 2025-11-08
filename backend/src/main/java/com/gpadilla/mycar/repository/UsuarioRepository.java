package com.gpadilla.mycar.repository;


import com.gpadilla.mycar.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByEmailAndEliminadoFalse(String name);
    boolean existsByEmailAndIdNotAndEliminadoFalse(String name, Long id);

    Optional<Usuario> findByIdAndEliminadoFalse(Long id);
    Optional<Usuario> findByEmailAndEliminadoFalse(String nombre);
    Optional<Usuario> findByProviderIdAndEliminadoFalse(String providerId);
}
