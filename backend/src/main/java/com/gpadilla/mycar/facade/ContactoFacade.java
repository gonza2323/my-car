package com.gpadilla.mycar.facade;

import com.gpadilla.mycar.dtos.contacto.ContactoCreateOrUpdateDto;
import com.gpadilla.mycar.dtos.contacto.ContactoCorreoDetailDto;
import com.gpadilla.mycar.dtos.contacto.ContactoTelefonicoDetailDto;
import com.gpadilla.mycar.dtos.contacto.ContactosDetalleDto;
import com.gpadilla.mycar.entity.Contacto;
import com.gpadilla.mycar.entity.ContactoCorreoElectronico;
import com.gpadilla.mycar.entity.ContactoTelefonico;
import com.gpadilla.mycar.error.BusinessException;
import com.gpadilla.mycar.repository.ContactoRepository;
import com.gpadilla.mycar.repository.UsuarioRepository;
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
    private final UsuarioRepository usuarioRepository;

    /* =================== Consultas =================== */

    @Transactional(readOnly = true)
    public ContactosDetalleDto contactosDeUsuario(Long usuarioId) {

        if (!usuarioRepository.existsById(usuarioId)) {
            throw new BusinessException("Usuario no encontrado");
        }
        var telefono = telefonoService.findActivoPorUsuario(usuarioId).orElse(null);
        var correo   = correoService.findActivoPorUsuario(usuarioId).orElse(null);
        ContactosDetalleDto dto = new ContactosDetalleDto();
        dto.setTelefono(telefono);
        dto.setCorreo(correo);
        return dto;
    }


    @Transactional(readOnly = true)
    public ContactosDetalleDto contactosDeEmpresa() {
        System.out.println("trayendo telefono");
        var telefono = telefonoService.findActivoPorEmpresa().orElse(null);
        System.out.println("trayendo correo");
        var correo   = correoService.findActivoPorEmpresa().orElse(null);

        ContactosDetalleDto dto = new ContactosDetalleDto();
        dto.setTelefono(telefono);
        dto.setCorreo(correo);
        return dto;
    }



    @Transactional(readOnly = true)
    public Object obtenerDetalle(Long contactoId) {
        Contacto contacto = contactoRepository.findByIdAndEliminadoFalse(contactoId)
                .orElseThrow(() -> new BusinessException("Contacto no encontrado"));

        if (contacto instanceof ContactoTelefonico) {
            return telefonoService.findDto(contactoId);
        }
        if (contacto instanceof ContactoCorreoElectronico) {
            return correoService.findDto(contactoId);
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

        return new ContactosDetalleDto(telefono, correo);
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

        return new ContactosDetalleDto(telefono, correo);
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

    /* =================== Eliminación (soft delete) =================== */

    @Transactional
    public void eliminar(Long contactoId) {
        try {
            telefonoService.delete(contactoId);
        } catch (BusinessException e) {
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

    @Transactional(readOnly = true)
    public ContactoTelefonicoDetailDto getTelefonoDto(Long id) {
        return telefonoService.findDto(id);
    }

    @Transactional(readOnly = true)
    public ContactoCorreoDetailDto getCorreoDto(Long id) {
        return correoService.findDto(id);
    }

    @Transactional(readOnly = true)
    public Object getContactoDto(Long id) {
        try { return getTelefonoDto(id); }
        catch (BusinessException e) { return getCorreoDto(id); }
    }

    @Transactional
    public void eliminarContacto(Long id) {
        try { telefonoService.delete(id); }
        catch (BusinessException e) { correoService.delete(id); }
    }


}
