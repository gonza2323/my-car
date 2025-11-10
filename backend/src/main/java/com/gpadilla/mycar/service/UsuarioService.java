package com.gpadilla.mycar.service;

import com.gpadilla.mycar.config.AppProperties;
import com.gpadilla.mycar.dtos.usuario.UsuarioDetailDto;
import com.gpadilla.mycar.entity.Usuario;
import com.gpadilla.mycar.enums.UserRole;
import com.gpadilla.mycar.error.BusinessException;
import com.gpadilla.mycar.mapper.UsuarioMapper;
import com.gpadilla.mycar.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final AppProperties properties;
    private final UsuarioMapper usuarioMapper;

    @Transactional(readOnly = true)
    public Usuario find(Long id) {
        return repository.findByIdAndEliminadoFalse(id)
                .orElseThrow(() -> new BusinessException("Usuario no encontrado"));
    }

    @Transactional(readOnly = true)
    public UsuarioDetailDto findDto(Long id) {
        return usuarioMapper.toDto(find(id));
    }

    // clientes o empleados (desde admin)
    @Transactional
    public Usuario createUserNoPassword(String email, UserRole rol) {
        String defaultPassword = properties.auth().defaultPassword();
        String passwordHash = passwordEncoder.encode(defaultPassword);

        Usuario usuario = Usuario.builder()
                .email(email)
                .password(passwordHash)
                .rol(rol)
                .hasCompletedProfile(true)
                .mustChangePassword(true)
                .build();

        return repository.save(usuario);
    }

    // solo clientes
    @Transactional
    public Usuario createUserFromEmailPassword(String email, String password, String passwordConfirm) {
        validarEmail(email);
        validarClave(password, passwordConfirm);

        String passwordHash = passwordEncoder.encode(password);

        Usuario usuario = Usuario.builder()
                .email(email)
                .password(passwordHash)
                .rol(UserRole.CLIENTE)
                .hasCompletedProfile(false)
                .mustChangePassword(false)
                .build();

        return repository.save(usuario);
    }

    // solo clientes
    @Transactional
    public Usuario createUserFromProvider(String providerId, String email) {
        validarEmail(email);
        
        Usuario usuario = Usuario.builder()
                .email(email)
                .password(null)
                .providerId(providerId)
                .rol(UserRole.CLIENTE)
                .hasCompletedProfile(false)
                .mustChangePassword(false)
                .build();

        return repository.save(usuario);
    }

    private void validarClave(String password, String passwordConfirm) {
        if (!password.equals(passwordConfirm))
            throw new BusinessException("Las contraseñas no coinciden");
    }

    private void validarEmail(String nombre) {
        boolean taken = repository.existsByEmailAndEliminadoFalse(nombre);
        if (taken)
            throw new BusinessException("El email de usuario ya está en uso");
    }

    public void delete(Usuario usuario) {
        usuario.setEliminado(true);
    }
}
