package com.gpadilla.mycar.repository;


import com.gpadilla.mycar.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

}
