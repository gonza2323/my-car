package com.gpadilla.mycar.controller;

import com.gpadilla.mycar.auth.CurrentUser;
import com.gpadilla.mycar.dtos.contacto.ContactoCreateOrUpdateDto;
import com.gpadilla.mycar.dtos.contacto.ContactoCorreoDetailDto;
import com.gpadilla.mycar.dtos.contacto.ContactoTelefonicoDetailDto;
import com.gpadilla.mycar.dtos.contacto.ContactosDetalleDto;
import com.gpadilla.mycar.enums.UserRole;
import com.gpadilla.mycar.error.BusinessException;
import com.gpadilla.mycar.facade.ContactoFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/contacto")
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class ContactoController {

    private final ContactoFacade facade;

    /* =================== Contactos propios =================== */

    @GetMapping("/mis-datos")
    @PreAuthorize("hasAnyRole('CLIENTE','JEFE','ADMINISTRATIVO')")
    public ResponseEntity<ContactosDetalleDto> obtenerMisContactos(
            @AuthenticationPrincipal CurrentUser user
    ) {
        return ResponseEntity.ok(facade.contactosDeUsuario(user.getId()));
    }

    @PutMapping("/mis-datos")
    @PreAuthorize("hasAnyRole('CLIENTE','JEFE','ADMINISTRATIVO')")
    public ResponseEntity<ContactosDetalleDto> actualizarMisContactos(
            @AuthenticationPrincipal CurrentUser user,
            @Valid @RequestBody ContactoCreateOrUpdateDto dto
    ) {
        return ResponseEntity.ok(facade.actualizarContactosDeUsuario(user.getId(), dto));
    }

    /* =================== Empresa =================== */

    @GetMapping("/empresa")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ContactosDetalleDto> obtenerContactosEmpresa() {
        return ResponseEntity.ok(facade.contactosDeEmpresa());
    }

    @PostMapping("/empresa/contactos")
    @PreAuthorize("hasRole('JEFE')")
    public ResponseEntity<ContactosDetalleDto> upsertContactosEmpresa(
            @Valid @RequestBody ContactoCreateOrUpdateDto dto
    ) {
        ContactosDetalleDto detalle = facade.actualizarContactosDeEmpresa(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(detalle);
    }

    /* =================== Administración de usuarios =================== */

    @GetMapping("/usuario/{usuarioId}")
    @PreAuthorize("hasAnyRole('JEFE','ADMINISTRATIVO')")
    public ResponseEntity<ContactosDetalleDto> contactosDeUsuario(
            @PathVariable Long usuarioId
    ) {
        return ResponseEntity.ok(facade.contactosDeUsuario(usuarioId));
    }

    @PutMapping("/usuario/{usuarioId}")
    @PreAuthorize("hasAnyRole('JEFE','ADMINISTRATIVO')")
    public ResponseEntity<ContactosDetalleDto> actualizarContactosDeUsuario(
            @PathVariable Long usuarioId,
            @Valid @RequestBody ContactoCreateOrUpdateDto dto
    ) {
        return ResponseEntity.ok(facade.actualizarContactosDeUsuario(usuarioId, dto));
    }


    @PostMapping("/usuario/{usuarioId}/correo")
    @PreAuthorize("hasAnyRole('JEFE','ADMINISTRATIVO')")
    public ResponseEntity<ContactoCorreoDetailDto> crearCorreoParaUsuario(
            @PathVariable Long usuarioId,
            @Valid @RequestBody ContactoCreateOrUpdateDto dto
    ) {
        ContactoCorreoDetailDto detalle = facade.crearCorreoParaUsuario(usuarioId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(detalle);
    }

    @PostMapping("/usuario/{usuarioId}/telefonico")
    @PreAuthorize("hasAnyRole('JEFE','ADMINISTRATIVO')")
    public ResponseEntity<ContactoTelefonicoDetailDto> crearTelefonoParaUsuario(
            @PathVariable Long usuarioId,
            @Valid @RequestBody ContactoCreateOrUpdateDto dto
    ) {
        ContactoTelefonicoDetailDto detalle = facade.crearTelefonoParaUsuario(usuarioId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(detalle);
    }

    /* =================== CRUD por id =================== */

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Object> obtenerContacto(
            @PathVariable Long id,
            @AuthenticationPrincipal CurrentUser user
    ) {
        Object dto = facade.obtenerDetalle(id); // Tel o Correo (DTO concreto)
        if (!puedeVer(user, dto)) {
            throw new BusinessException("No tiene permisos para ver este contacto");
        }
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> eliminarContacto(
            @PathVariable Long id,
            @AuthenticationPrincipal CurrentUser user
    ) {
        Object dto = facade.obtenerDetalle(id);
        if (!puedeModificar(user, dto)) {
            throw new BusinessException("No tiene permisos para eliminar este contacto");
        }
        facade.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    /* =================== Helpers =================== */

    private boolean esJefeOAdmin(CurrentUser user) {
        return user != null && user.getRoles().stream()
                .anyMatch(r -> r == UserRole.JEFE || r == UserRole.ADMINISTRATIVO);
    }
    private boolean esJefe(CurrentUser user) {
        return user != null && user.getRoles().contains(UserRole.JEFE);
    }

    private Long empresaId(Object dto) {
        if (dto instanceof ContactoTelefonicoDetailDto t) return t.getEmpresaId();
        if (dto instanceof ContactoCorreoDetailDto c)    return c.getEmpresaId();
        return null;
    }
    private Long usuarioId(Object dto) {
        if (dto instanceof ContactoTelefonicoDetailDto t) return t.getUsuarioId();
        if (dto instanceof ContactoCorreoDetailDto c)    return c.getUsuarioId();
        return null;
    }

    private boolean puedeVer(CurrentUser user, Object dto) {
        if (empresaId(dto) != null) return true; // empresa: visible autenticados
        Long owner = usuarioId(dto);
        return user != null && (owner != null && owner.equals(user.getId()) || esJefeOAdmin(user));
    }

    private boolean puedeModificar(CurrentUser user, Object dto) {
        if (empresaId(dto) != null) return esJefe(user); // empresa: solo JEFE
        Long owner = usuarioId(dto);
        return user != null && (owner != null && owner.equals(user.getId()) || esJefeOAdmin(user));
    }
}
