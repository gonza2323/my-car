package com.gpadilla.mycar.service;

import com.gpadilla.mycar.dtos.cliente.ClienteCreateDto;
import com.gpadilla.mycar.entity.geo.Direccion;
import com.gpadilla.mycar.entity.Cliente;
import com.gpadilla.mycar.entity.Nacionalidad;
import com.gpadilla.mycar.entity.Usuario;
import com.gpadilla.mycar.error.BusinessException;
import com.gpadilla.mycar.mapper.ClienteMapper;
import com.gpadilla.mycar.repository.ClienteRepository;
import com.gpadilla.mycar.repository.NacionalidadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClienteService {
    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;
    private final NacionalidadRepository nacionalidadRepository;

    @Transactional
    public Cliente create(ClienteCreateDto dto, Usuario usuario, Direccion direccion) {
        Nacionalidad nacionalidad = nacionalidadRepository.findById(dto.getNacionalidadId())
                .orElseThrow(() -> new BusinessException("No se encontró la nacionalidad"));

        Cliente cliente = clienteMapper.toEntity(dto);
        cliente.setUsuario(usuario);
        cliente.setDireccion(direccion);
        cliente.setNacionalidad(nacionalidad);
        return clienteRepository.save(cliente);
    }
}
