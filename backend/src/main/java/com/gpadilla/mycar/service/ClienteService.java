package com.gpadilla.mycar.service;

import com.gpadilla.mycar.dtos.cliente.ClienteCreateDto;
import com.gpadilla.mycar.dtos.cliente.ClienteSummaryDto;
import com.gpadilla.mycar.entity.geo.Direccion;
import com.gpadilla.mycar.entity.Cliente;
import com.gpadilla.mycar.entity.geo.Nacionalidad;
import com.gpadilla.mycar.entity.Usuario;
import com.gpadilla.mycar.error.BusinessException;
import com.gpadilla.mycar.mapper.ClienteMapper;
import com.gpadilla.mycar.repository.ClienteRepository;
import com.gpadilla.mycar.repository.geo.NacionalidadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Transactional(readOnly = true)
    public Page<ClienteSummaryDto> findDtos(Pageable pageable) {
        return clienteRepository.buscarResumenClientes(pageable);
    }

    @Transactional(readOnly = true)
    public Cliente find(Long id) {
        return clienteRepository.findByIdAndEliminadoFalse(id)
                .orElseThrow(() -> new BusinessException("Cliente no encontrado"));
    }

    @Transactional
    public Cliente delete(Long id) {
        Cliente cliente = find(id);
        cliente.setEliminado(true);
        return clienteRepository.save(cliente);
    }
}
