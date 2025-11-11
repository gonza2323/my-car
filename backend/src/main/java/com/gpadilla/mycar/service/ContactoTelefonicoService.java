package com.gpadilla.mycar.service;

import com.gpadilla.mycar.dtos.contacto.ContactoCreateOrUpdateDto;
import com.gpadilla.mycar.dtos.contacto.ContactoTelefonicoDetailDto;
import com.gpadilla.mycar.entity.Contacto;
import com.gpadilla.mycar.entity.ContactoTelefonico;
import com.gpadilla.mycar.entity.Empresa;
import com.gpadilla.mycar.entity.Usuario;
import com.gpadilla.mycar.enums.TipoContacto;
import com.gpadilla.mycar.error.BusinessException;
import com.gpadilla.mycar.mapper.ContactoTelefonicoMapper;
import com.gpadilla.mycar.repository.ContactoRepository;
import com.gpadilla.mycar.repository.EmpresaRepository;
import com.gpadilla.mycar.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ContactoTelefonicoService {

    private final ContactoRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final EmpresaRepository empresaRepository;
    private final ContactoTelefonicoMapper mapper;

    @Transactional(readOnly = true)
    public ContactoTelefonico find(Long id) {
        Contacto contacto = repository.findByIdAndEliminadoFalse(id)
                .orElseThrow(() -> new BusinessException("Contacto telefónico no encontrado"));
        if (contacto instanceof ContactoTelefonico telefono) {
            return telefono;
        }
        throw new BusinessException("El contacto solicitado no es telefónico");
    }

    @Transactional(readOnly = true)
    public ContactoTelefonicoDetailDto findDto(Long id) {
        return mapper.toDto(find(id));
    }

    @Transactional(readOnly = true)
    public Optional<ContactoTelefonicoDetailDto> findActivoPorUsuario(Long usuarioId) {
        return repository.findTelefonoVigenteByUsuarioId(usuarioId)
                .map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<ContactoTelefonicoDetailDto> findActivoPorEmpresa() {
        return empresaRepository.findFirstByEliminadoFalseOrderByIdAsc()
                .flatMap(empresa -> repository.findTelefonoVigenteByEmpresaId(empresa.getId()))
                .map(mapper::toDto);
    }

    public ContactoTelefonicoDetailDto upsertParaUsuario(Long usuarioId, ContactoCreateOrUpdateDto dto) {
        Usuario usuario = usuarioRepository.findByIdAndEliminadoFalse(usuarioId)
                .orElseThrow(() -> new BusinessException("Usuario no encontrado"));
        ContactoTelefonico actual = repository
                .findTelefonoVigenteByUsuarioId(usuarioId)
                .orElse(null);
        return guardarTelefono(dto, actual, usuario, null);
    }

    public ContactoTelefonicoDetailDto upsertParaEmpresa(ContactoCreateOrUpdateDto dto) {
        Empresa empresa = obtenerEmpresaObligatoria();
        ContactoTelefonico actual = repository
                .findTelefonoVigenteByEmpresaId(empresa.getId())
                .orElse(null);
        return guardarTelefono(dto, actual, null, empresa);
    }

    public ContactoTelefonicoDetailDto crearParaUsuario(Long usuarioId, ContactoCreateOrUpdateDto dto) {
        return upsertParaUsuario(usuarioId, dto);
    }

    public ContactoTelefonicoDetailDto crearParaEmpresa(ContactoCreateOrUpdateDto dto) {
        return upsertParaEmpresa(dto);
    }

    public void delete(Long id) {
        ContactoTelefonico entity = find(id);
        marcarEliminado(entity);
    }

    private ContactoTelefonicoDetailDto guardarTelefono(
            ContactoCreateOrUpdateDto dto,
            ContactoTelefonico actual,
            Usuario usuario,
            Empresa empresa
    ) {
        validarTelefono(dto);
        String telefonoNormalizado = normalizarTelefono(dto.getTelefono());
        if (actual != null) {
            if (telefonoNormalizado.equals(actual.getTelefono())) {
                actualizarEntidad(actual, dto, telefonoNormalizado);
                return mapper.toDto(repository.save(actual));
            }
            marcarEliminado(actual);
        }
        ContactoTelefonico nuevo = mapper.toEntity(dto);
        nuevo.setUsuario(usuario);
        nuevo.setEmpresa(empresa);
        nuevo.setTelefono(telefonoNormalizado);
        nuevo.setEliminado(false);
        aplicarDefaults(nuevo, dto);
        return mapper.toDto(repository.save(nuevo));
    }

    private void actualizarEntidad(ContactoTelefonico entity, ContactoCreateOrUpdateDto dto, String telefonoNormalizado) {
        mapper.updateEntity(dto, entity);
        entity.setTelefono(telefonoNormalizado);
        aplicarDefaults(entity, dto);
    }

    private void aplicarDefaults(Contacto contacto, ContactoCreateOrUpdateDto dto) {
        if (contacto.getTipoContacto() == null) {
            contacto.setTipoContacto(dto.getTipoContacto());
        }
        if (contacto.getTipoContacto() == null) {
            contacto.setTipoContacto(TipoContacto.LABORAL);
        }
        if (dto.getObservacion() != null) {
            contacto.setObservacion(dto.getObservacion().trim());
        }
        if (contacto.getObservacion() == null) {
            contacto.setObservacion("");
        }
    }

    private void marcarEliminado(Contacto contacto) {
        contacto.setEliminado(true);
        repository.save(contacto);
    }

    private void validarTelefono(ContactoCreateOrUpdateDto dto) {
        if (dto.getTelefono() == null || dto.getTelefono().isBlank()) {
            throw new BusinessException("El teléfono es obligatorio");
        }
        if (dto.getTipoTelefono() == null) {
            throw new BusinessException("El tipo de teléfono es obligatorio");
        }
    }

    private Empresa obtenerEmpresaObligatoria() {
        return empresaRepository.findFirstByEliminadoFalseOrderByIdAsc()
                .orElseThrow(() -> new BusinessException("La empresa aún no fue inicializada."));
    }

    private String normalizarTelefono(String raw) {
        String valor = raw == null ? "" : raw.trim();
        boolean internacional = valor.startsWith("+");
        String soloNumeros = valor.replaceAll("[^0-9]", "");
        if (soloNumeros.isBlank()) {
            throw new BusinessException("El teléfono es obligatorio");
        }
        return internacional ? "+" + soloNumeros : soloNumeros;
    }
}
