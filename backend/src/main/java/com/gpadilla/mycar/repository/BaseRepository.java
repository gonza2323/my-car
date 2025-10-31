package com.gpadilla.mycar.repository;

import com.gpadilla.mycar.entity.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository<E extends BaseEntity<ID>, ID extends Serializable> extends JpaRepository<E, ID> {
    Optional<E> findByIdAndEliminadoFalse(ID id);
    Page<E> findByEliminadoFalse(Pageable pageable);
}
