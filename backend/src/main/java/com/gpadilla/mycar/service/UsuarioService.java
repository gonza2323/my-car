package com.gpadilla.mycar.service;

import com.gpadilla.mycar.dtos.usuario.UsuarioCreateDto;
import com.gpadilla.mycar.dtos.usuario.UsuarioDetailDto;
import com.gpadilla.mycar.dtos.usuario.UsuarioSummaryDto;
import com.gpadilla.mycar.dtos.usuario.UsuarioUpdateDto;
import com.gpadilla.mycar.entity.Usuario;
import com.gpadilla.mycar.enums.UserRole;
import com.gpadilla.mycar.error.BusinessException;
import com.gpadilla.mycar.mapper.UsuarioMapper;
import com.gpadilla.mycar.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService extends BaseService<
        Usuario,
        Long,
        UsuarioRepository,
        UsuarioDetailDto,
        UsuarioSummaryDto,
        UsuarioCreateDto,
        UsuarioUpdateDto,
        UsuarioMapper> {

    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository repository, UsuarioMapper mapper, PasswordEncoder passwordEncoder) {
        super("Usuario", repository, mapper);
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void preCreate(UsuarioCreateDto dto, Usuario usuario) {
        String passwordHash = passwordEncoder.encode(dto.getPassword());
        usuario.setPassword(passwordHash);
    }

    @Override
    protected void validateCreate(UsuarioCreateDto dto) {
        validarEmailUnico(dto.getEmail(), null);

        if (!dto.getPassword().equals(dto.getPasswordConfirmacion()))
            throw new BusinessException("Las contraseñas no coinciden");
    }

    @Override
    protected void validateUpdate(UsuarioUpdateDto dto) {
        validarEmailUnico(dto.getEmail(), dto.getId());
    }

    public void validarEmailUnico(String nombre, Long excludeId) {
        boolean exists = (excludeId == null)
                ? repository.existsByEmailAndEliminadoFalse(nombre)
                : repository.existsByEmailAndIdNotAndEliminadoFalse(nombre, excludeId);

        if (exists)
            throw new BusinessException("El email de usuario ya está en uso");
    }

    @Transactional
    public Usuario registerUserFromAuth0(String providerId, String email) {
        validarEmailUnico(email, null);
        
        Usuario usuario = Usuario.builder()
                .email(email)
                .password(null)
                .providerId(providerId)
                .rol(UserRole.CLIENTE)
                .hasCompletedProfile(false).build();

        repository.save(usuario);
        return usuario;
    }
}
