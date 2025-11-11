package com.gpadilla.mycar.controller;

import com.gpadilla.mycar.auth.CurrentUser;
import com.gpadilla.mycar.dtos.contacto.ContactoCreateOrUpdateDto;
import com.gpadilla.mycar.dtos.contacto.ContactoCorreoDetailDto;
import com.gpadilla.mycar.dtos.contacto.ContactoTelefonicoDetailDto;
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
    public ResponseEntity<ContactoFacade.ContactosDetalleDto> obtenerMisContactos(
            @AuthenticationPrincipal CurrentUser user
    ) {
        return ResponseEntity.ok(facade.contactosDeUsuario(user.getId()));
    }

    @PutMapping("/mis-datos")
    @PreAuthorize("hasAnyRole('CLIENTE','JEFE','ADMINISTRATIVO')")
    public ResponseEntity<ContactoFacade.ContactosDetalleDto> actualizarMisContactos(
            @AuthenticationPrincipal CurrentUser user,
            @Valid @RequestBody ContactoCreateOrUpdateDto dto
    ) {
        return ResponseEntity.ok(facade.actualizarContactosDeUsuario(user.getId(), dto));
    }

    /* =================== Empresa =================== */

    @GetMapping("/empresa")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ContactoFacade.ContactosDetalleDto> obtenerContactosEmpresa() {
        return ResponseEntity.ok(facade.contactosDeEmpresa());
    }

    @PutMapping("/empresa")
    @PreAuthorize("hasRole('JEFE')")
    public ResponseEntity<ContactoFacade.ContactosDetalleDto> actualizarContactosEmpresa(
            @Valid @RequestBody ContactoCreateOrUpdateDto dto
    ) {
        return ResponseEntity.ok(facade.actualizarContactosDeEmpresa(dto));
    }

    @PostMapping("/empresa/correo")
    @PreAuthorize("hasRole('JEFE')")
    public ResponseEntity<ContactoCorreoDetailDto> crearCorreoEmpresa(
            @Valid @RequestBody ContactoCreateOrUpdateDto dto
    ) {
        ContactoCorreoDetailDto detalle = facade.crearCorreoParaEmpresa(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(detalle);
    }

    @PostMapping("/empresa/telefonico")
    @PreAuthorize("hasRole('JEFE')")
    public ResponseEntity<ContactoTelefonicoDetailDto> crearTelefonoEmpresa(
            @Valid @RequestBody ContactoCreateOrUpdateDto dto
    ) {
        ContactoTelefonicoDetailDto detalle = facade.crearTelefonoParaEmpresa(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(detalle);
    }

    /* =================== Administración de usuarios =================== */

    @GetMapping("/usuario/{usuarioId}")
    @PreAuthorize("hasAnyRole('JEFE','ADMINISTRATIVO')")
    public ResponseEntity<ContactoFacade.ContactosDetalleDto> contactosDeUsuario(
            @PathVariable Long usuarioId
    ) {
        return ResponseEntity.ok(facade.contactosDeUsuario(usuarioId));
    }

    @PutMapping("/usuario/{usuarioId}")
    @PreAuthorize("hasAnyRole('JEFE','ADMINISTRATIVO')")
    public ResponseEntity<ContactoFacade.ContactosDetalleDto> actualizarContactosDeUsuario(
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
        ContactoFacade.ContactoDetalleWrapper detalle = facade.obtenerDetalle(id);
        if (!puedeVer(user, detalle)) {
            throw new BusinessException("No tiene permisos para ver este contacto");
        }
        return ResponseEntity.ok(detalle.payload());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> eliminarContacto(
            @PathVariable Long id,
            @AuthenticationPrincipal CurrentUser user
    ) {
        ContactoFacade.ContactoDetalleWrapper detalle = facade.obtenerDetalle(id);
        if (!puedeModificar(user, detalle)) {
            throw new BusinessException("No tiene permisos para eliminar este contacto");
        }
        facade.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    /* =================== Helpers =================== */

    private boolean puedeVer(CurrentUser user, ContactoFacade.ContactoDetalleWrapper detalle) {
        if (detalle.esDeEmpresa()) {
            return true;
        }
        if (user == null) {
            return false;
        }
        return detalle.perteneceAUsuario(user.getId()) || esJefeOAdmin(user);
    }

    private boolean puedeModificar(CurrentUser user, ContactoFacade.ContactoDetalleWrapper detalle) {
        if (detalle.esDeEmpresa()) {
            return esJefe(user);
        }
        if (user == null) {
            return false;
        }
        return detalle.perteneceAUsuario(user.getId()) || esJefeOAdmin(user);
    }

    private boolean esJefeOAdmin(CurrentUser user) {
        return user != null && user.getRoles().stream()
                .anyMatch(rol -> rol == UserRole.JEFE || rol == UserRole.ADMINISTRATIVO);
    }

    private boolean esJefe(CurrentUser user) {
        return user != null && user.getRoles().contains(UserRole.JEFE);
    }
}
