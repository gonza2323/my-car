package com.gpadilla.mycar.facade;

import com.gpadilla.mycar.dtos.contacto.ContactoCreateOrUpdateDto;
import com.gpadilla.mycar.dtos.contacto.ContactoCorreoDetailDto;
import com.gpadilla.mycar.dtos.contacto.ContactoTelefonicoDetailDto;
import com.gpadilla.mycar.entity.Contacto;
import com.gpadilla.mycar.entity.ContactoCorreoElectronico;
import com.gpadilla.mycar.entity.ContactoTelefonico;
import com.gpadilla.mycar.error.BusinessException;
import com.gpadilla.mycar.repository.ContactoRepository;
import com.gpadilla.mycar.service.ContactoCorreoService;
import com.gpadilla.mycar.service.ContactoTelefonicoService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ContactoFacade {

    private final ContactoTelefonicoService telefonoService;
    private final ContactoCorreoService correoService;
    private final ContactoRepository contactoRepository;

    /* =================== Consultas =================== */

    @Transactional(readOnly = true)
    public ContactosDetalleDto contactosDeUsuario(Long usuarioId) {
        return ContactosDetalleDto.of(
                telefonoService.findActivoPorUsuario(usuarioId).orElse(null),
                correoService.findActivoPorUsuario(usuarioId).orElse(null)
        );
    }

    @Transactional(readOnly = true)
    public ContactosDetalleDto contactosDeEmpresa() {
        return ContactosDetalleDto.of(
                telefonoService.findActivoPorEmpresa().orElse(null),
                correoService.findActivoPorEmpresa().orElse(null)
        );
    }

    @Transactional(readOnly = true)
    public ContactoDetalleWrapper obtenerDetalle(Long contactoId) {
        Contacto contacto = contactoRepository.findByIdAndEliminadoFalse(contactoId)
                .orElseThrow(() -> new BusinessException("Contacto no encontrado"));
        if (contacto instanceof ContactoTelefonico) {
            return ContactoDetalleWrapper.telefono(telefonoService.findDto(contactoId));
        }
        if (contacto instanceof ContactoCorreoElectronico) {
            return ContactoDetalleWrapper.correo(correoService.findDto(contactoId));
        }
        throw new BusinessException("Tipo de contacto no soportado");
    }

    /* =================== Actualizaciones idempotentes =================== */

    @Transactional
    public ContactosDetalleDto actualizarContactosDeUsuario(Long usuarioId, ContactoCreateOrUpdateDto dto) {
        validarContenido(dto);
        ContactoTelefonicoDetailDto telefono = tieneTelefono(dto)
                ? telefonoService.upsertParaUsuario(usuarioId, dto)
                : telefonoService.findActivoPorUsuario(usuarioId).orElse(null);
        ContactoCorreoDetailDto correo = tieneEmail(dto)
                ? correoService.upsertParaUsuario(usuarioId, dto)
                : correoService.findActivoPorUsuario(usuarioId).orElse(null);
        return ContactosDetalleDto.of(telefono, correo);
    }

    @Transactional
    public ContactosDetalleDto actualizarContactosDeEmpresa(ContactoCreateOrUpdateDto dto) {
        validarContenido(dto);
        ContactoTelefonicoDetailDto telefono = tieneTelefono(dto)
                ? telefonoService.upsertParaEmpresa(dto)
                : telefonoService.findActivoPorEmpresa().orElse(null);
        ContactoCorreoDetailDto correo = tieneEmail(dto)
                ? correoService.upsertParaEmpresa(dto)
                : correoService.findActivoPorEmpresa().orElse(null);
        return ContactosDetalleDto.of(telefono, correo);
    }

    /* =================== Creaciones explícitas =================== */

    @Transactional
    public ContactoCorreoDetailDto crearCorreoParaUsuario(Long usuarioId, ContactoCreateOrUpdateDto dto) {
        if (!tieneEmail(dto)) {
            throw new BusinessException("El email es obligatorio");
        }
        return correoService.crearParaUsuario(usuarioId, dto);
    }

    @Transactional
    public ContactoTelefonicoDetailDto crearTelefonoParaUsuario(Long usuarioId, ContactoCreateOrUpdateDto dto) {
        if (!tieneTelefono(dto)) {
            throw new BusinessException("El teléfono es obligatorio");
        }
        return telefonoService.crearParaUsuario(usuarioId, dto);
    }

    @Transactional
    public ContactoCorreoDetailDto crearCorreoParaEmpresa(ContactoCreateOrUpdateDto dto) {
        if (!tieneEmail(dto)) {
            throw new BusinessException("El email es obligatorio");
        }
        return correoService.crearParaEmpresa(dto);
    }

    @Transactional
    public ContactoTelefonicoDetailDto crearTelefonoParaEmpresa(ContactoCreateOrUpdateDto dto) {
        if (!tieneTelefono(dto)) {
            throw new BusinessException("El teléfono es obligatorio");
        }
        return telefonoService.crearParaEmpresa(dto);
    }

    /* =================== Eliminación (soft delete) =================== */

    @Transactional
    public void eliminar(Long contactoId) {
        ContactoDetalleWrapper detalle = obtenerDetalle(contactoId);
        if (detalle.esTelefonico()) {
            telefonoService.delete(contactoId);
        } else {
            correoService.delete(contactoId);
        }
    }

    /* =================== Helpers =================== */

    private void validarContenido(ContactoCreateOrUpdateDto dto) {
        if (!tieneTelefono(dto) && !tieneEmail(dto)) {
            throw new BusinessException("Debe indicar al menos un dato de contacto.");
        }
    }

    private boolean tieneEmail(ContactoCreateOrUpdateDto dto) {
        return dto.getEmail() != null && !dto.getEmail().isBlank();
    }

    private boolean tieneTelefono(ContactoCreateOrUpdateDto dto) {
        return dto.getTelefono() != null && !dto.getTelefono().isBlank();
    }

    /* =================== DTOs de respuesta =================== */

    @Getter
    public static class ContactosDetalleDto {
        private final ContactoTelefonicoDetailDto telefono;
        private final ContactoCorreoDetailDto correo;

        private ContactosDetalleDto(ContactoTelefonicoDetailDto telefono, ContactoCorreoDetailDto correo) {
            this.telefono = telefono;
            this.correo = correo;
        }

        public static ContactosDetalleDto of(ContactoTelefonicoDetailDto telefono, ContactoCorreoDetailDto correo) {
            return new ContactosDetalleDto(telefono, correo);
        }
    }

    @Getter
    public static class ContactoDetalleWrapper {
        private final ContactoTelefonicoDetailDto telefonico;
        private final ContactoCorreoDetailDto correo;

        private ContactoDetalleWrapper(ContactoTelefonicoDetailDto telefonico, ContactoCorreoDetailDto correo) {
            this.telefonico = telefonico;
            this.correo = correo;
        }

        public static ContactoDetalleWrapper telefono(ContactoTelefonicoDetailDto dto) {
            return new ContactoDetalleWrapper(dto, null);
        }

        public static ContactoDetalleWrapper correo(ContactoCorreoDetailDto dto) {
            return new ContactoDetalleWrapper(null, dto);
        }

        public Object payload() {
            return telefonico != null ? telefonico : correo;
        }

        public boolean esTelefonico() {
            return telefonico != null;
        }

        public Long usuarioId() {
            return telefonico != null
                    ? telefonico.getUsuarioId()
                    : correo != null ? correo.getUsuarioId() : null;
        }

        public Long empresaId() {
            return telefonico != null
                    ? telefonico.getEmpresaId()
                    : correo != null ? correo.getEmpresaId() : null;
        }

        public boolean perteneceAUsuario(Long usuarioId) {
            Long ownerId = usuarioId();
            return ownerId != null && ownerId.equals(usuarioId);
        }

        public boolean esDeEmpresa() {
            return empresaId() != null;
        }
    }
}
