package com.gpadilla.mycar.facade;

import com.gpadilla.mycar.dtos.cliente.ClienteCompleteProfileDto;
import com.gpadilla.mycar.dtos.cliente.ClienteCreateDto;
import com.gpadilla.mycar.dtos.cliente.ClienteCreateRequestDto;
import com.gpadilla.mycar.dtos.cliente.SignUpFormDto;
import com.gpadilla.mycar.entity.Cliente;
import com.gpadilla.mycar.entity.Usuario;
import com.gpadilla.mycar.entity.geo.Direccion;
import com.gpadilla.mycar.enums.UserRole;
import com.gpadilla.mycar.mapper.ClienteMapper;
import com.gpadilla.mycar.service.ClienteService;
import com.gpadilla.mycar.service.UsuarioService;
import com.gpadilla.mycar.service.geo.DireccionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClienteFacade {

    private final DireccionService direccionService;
    private final UsuarioService usuarioService;
    private final ClienteService clienteService;
    private final ClienteMapper clienteMapper;

    @Transactional
    public Usuario registrarClientePorFormularioRegistro(SignUpFormDto request) {
        return usuarioService.createUserFromEmailPassword(request.getEmail(), request.getPassword(), request.getPasswordConfirm());
    }

    @Transactional
    public Long registrarClientePorFormularioAdmin(ClienteCreateRequestDto request) {
        // todo falta imagen
        // todo falta contacto

        Usuario usuario = usuarioService.createUserNoPassword(request.getEmail(), UserRole.CLIENTE);

        Direccion direccion = direccionService.create(request.getDireccion());

        ClienteCreateDto clienteDto = clienteMapper.toDto(request);
        Cliente cliente = clienteService.create(clienteDto, usuario, direccion);
        return cliente.getId();
    }

    @Transactional
    public void completarPerfilDeCliente(Long clienteId, ClienteCompleteProfileDto dto) {
        // todo falta imagen
        // todo falta contacto

        Usuario usuario = usuarioService.find(clienteId);

        Direccion direccion = direccionService.create(dto.getDireccion());

        ClienteCreateDto clienteDto = clienteMapper.toDto(dto);
        clienteService.create(clienteDto, usuario, direccion);

        usuario.setHasCompletedProfile(true);
    }

    public void borrarCliente(Long id) {
        // todo falta imagen
        // todo falta contacto

        Cliente cliente = clienteService.delete(id);
        usuarioService.delete(cliente.getUsuario());
        direccionService.delete(cliente.getDireccion());
    }
}
