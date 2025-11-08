package com.gpadilla.mycar.service;

import com.gpadilla.mycar.dtos.empleado.EmpleadoCreateDto;
import com.gpadilla.mycar.entity.Direccion;
import com.gpadilla.mycar.entity.Empleado;
import com.gpadilla.mycar.entity.Usuario;
import com.gpadilla.mycar.mapper.EmpleadoMapper;
import com.gpadilla.mycar.repository.EmpleadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmpleadoService {
    private final EmpleadoRepository empleadoRepository;
    private final EmpleadoMapper empleadoMapper;

    @Transactional
    public Empleado create(EmpleadoCreateDto dto, Usuario usuario, Direccion direccion) {
        Empleado empleado = empleadoMapper.toEntity(dto);
        empleado.setUsuario(usuario);
        empleado.setDireccion(direccion);
        return empleadoRepository.save(empleado);
    }
}
