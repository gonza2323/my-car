package com.gpadilla.mycar.service;

import com.gpadilla.mycar.dtos.contacto.ContactoCreateOrUpdateDto;
import com.gpadilla.mycar.dtos.contacto.ContactoCorreoDetailDto;
import com.gpadilla.mycar.entity.Contacto;
import com.gpadilla.mycar.entity.ContactoCorreoElectronico;
import com.gpadilla.mycar.entity.Empresa;
import com.gpadilla.mycar.entity.Usuario;
import com.gpadilla.mycar.enums.TipoContacto;
import com.gpadilla.mycar.error.BusinessException;
import com.gpadilla.mycar.mapper.ContactoCorreoMapper;
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
public class ContactoCorreoService {

    private final ContactoRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final EmpresaRepository empresaRepository;
    private final ContactoCorreoMapper mapper;

    @Transactional(readOnly = true)
    public ContactoCorreoElectronico find(Long id) {
        Contacto contacto = repository.findByIdAndEliminadoFalse(id)
                .orElseThrow(() -> new BusinessException("Contacto de correo no encontrado"));
        if (contacto instanceof ContactoCorreoElectronico correo) {
            return correo;
        }
        throw new BusinessException("El contacto solicitado no es de correo");
    }

    @Transactional(readOnly = true)
    public ContactoCorreoDetailDto findDto(Long id) {
        return mapper.toDto(find(id));
    }

    @Transactional(readOnly = true)
    public Optional<ContactoCorreoDetailDto> findActivoPorUsuario(Long usuarioId) {
        return repository.findCorreoVigenteByUsuarioId(usuarioId)
                .map(mapper::toDto);
    }

    // para obtener correo de empresa
    @Transactional(readOnly = true)
    public Optional<ContactoCorreoDetailDto> findActivoPorEmpresa() {
        Empresa empresa = obtenerEmpresaObligatoria();
        return repository.findCorreoVigenteByEmpresaId(empresa.getId())
                .map(mapper::toDto);
    }

    public ContactoCorreoDetailDto upsertParaUsuario(Long usuarioId, ContactoCreateOrUpdateDto dto) {
        Usuario usuario = usuarioRepository.findByIdAndEliminadoFalse(usuarioId)
                .orElseThrow(() -> new BusinessException("Usuario no encontrado"));
        ContactoCorreoElectronico actual = repository
                .findCorreoVigenteByUsuarioId(usuarioId)
                .orElse(null);
        return guardarCorreo(dto, actual, usuario, null);
    }

    public ContactoCorreoDetailDto upsertParaEmpresa(ContactoCreateOrUpdateDto dto) {
        Empresa empresa = obtenerEmpresaObligatoria();
        ContactoCorreoElectronico actual = repository
                .findCorreoVigenteByEmpresaId(empresa.getId())
                .orElse(null);
        return guardarCorreo(dto, actual, null, empresa);
    }

    public ContactoCorreoDetailDto crearParaUsuario(Long usuarioId, ContactoCreateOrUpdateDto dto) {
        return upsertParaUsuario(usuarioId, dto);
    }

    public ContactoCorreoDetailDto crearParaEmpresa(ContactoCreateOrUpdateDto dto) {
        return upsertParaEmpresa(dto);
    }

    public void delete(Long id) {
        ContactoCorreoElectronico entity = find(id);
        marcarEliminado(entity);
    }

    private ContactoCorreoDetailDto guardarCorreo(
            ContactoCreateOrUpdateDto dto,
            ContactoCorreoElectronico actual,
            Usuario usuario,
            Empresa empresa
    ) {
        String emailNormalizado = normalizarEmail(dto.getEmail());
        if (actual != null) {
            if (emailNormalizado.equalsIgnoreCase(actual.getEmail())) {
                actualizarEntidad(actual, dto, emailNormalizado);
                return mapper.toDto(repository.save(actual));
            }
            marcarEliminado(actual);
        }
        ContactoCorreoElectronico nuevo = mapper.toEntity(dto);
        nuevo.setUsuario(usuario);
        nuevo.setEmpresa(empresa);
        nuevo.setEmail(emailNormalizado);
        nuevo.setEliminado(false);
        aplicarDefaults(nuevo, dto);
        return mapper.toDto(repository.save(nuevo));
    }

    private void actualizarEntidad(ContactoCorreoElectronico entity, ContactoCreateOrUpdateDto dto, String emailNormalizado) {
        mapper.updateEntity(dto, entity);
        entity.setEmail(emailNormalizado);
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

    private Empresa obtenerEmpresaObligatoria() {
        return empresaRepository.findFirstByEliminadoFalseOrderByIdAsc()
                .orElseThrow(() -> new BusinessException("La empresa aún no fue inicializada."));
    }

    private String normalizarEmail(String email) {
        if (email == null || email.isBlank())
            throw new BusinessException("El email es obligatorio");
        return email.trim().toLowerCase();
    }
}
