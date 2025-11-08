package com.gpadilla.mycar.facade;

import com.gpadilla.mycar.dtos.empleado.EmpleadoCreateDto;
import com.gpadilla.mycar.dtos.empleado.EmpleadoCreateRequestDto;
import com.gpadilla.mycar.entity.Direccion;
import com.gpadilla.mycar.entity.Usuario;
import com.gpadilla.mycar.enums.UserRole;
import com.gpadilla.mycar.mapper.EmpleadoMapper;
import com.gpadilla.mycar.service.geo.DireccionService;
import com.gpadilla.mycar.service.EmpleadoService;
import com.gpadilla.mycar.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmpleadoFacade {

    private final EmpleadoMapper empleadoMapper;
    private final DireccionService direccionService;
    private final EmpleadoService empleadoService;
    private final UsuarioService usuarioService;

    @Transactional
    public void registrarEmpleado(EmpleadoCreateRequestDto request) {
        // todo falta imagen
        // todo falta contacto

        UserRole role = UserRole.valueOf(request.getTipoEmpleado().name());
        Usuario usuario = usuarioService.createUserNoPassword(request.getEmail(), role);

        Direccion direccion = direccionService.create(request.getDireccion());

        EmpleadoCreateDto empleadoDto = empleadoMapper.toDto(request);
        empleadoService.create(empleadoDto, usuario, direccion);
    }
}
