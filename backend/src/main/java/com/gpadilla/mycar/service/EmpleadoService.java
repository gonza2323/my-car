package com.gpadilla.mycar.service;

import com.gpadilla.mycar.dtos.empleado.EmpleadoCreateDto;
import com.gpadilla.mycar.dtos.empleado.EmpleadoSummaryDto;
import com.gpadilla.mycar.entity.geo.Direccion;
import com.gpadilla.mycar.entity.Empleado;
import com.gpadilla.mycar.entity.Usuario;
import com.gpadilla.mycar.error.BusinessException;
import com.gpadilla.mycar.mapper.EmpleadoMapper;
import com.gpadilla.mycar.repository.EmpleadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Transactional(readOnly = true)
    public Page<EmpleadoSummaryDto> findDtos(Pageable pageable) {
        return empleadoRepository.findAllByEliminadoFalse(pageable);
    }

    @Transactional(readOnly = true)
    public Empleado find(Long id) {
        return empleadoRepository.findByIdAndEliminadoFalse(id)
                .orElseThrow(() -> new BusinessException("Empleado no encontrado"));
    }

    @Transactional
    public Empleado delete(Long id) {
        Empleado empleado = find(id);
        empleado.setEliminado(true);
        return empleadoRepository.save(empleado);
    }
}
