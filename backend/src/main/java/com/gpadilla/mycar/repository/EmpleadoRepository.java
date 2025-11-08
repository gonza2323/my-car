package com.gpadilla.mycar.repository;


import com.gpadilla.mycar.entity.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {

}
