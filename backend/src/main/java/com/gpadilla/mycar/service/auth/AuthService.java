package com.gpadilla.mycar.service.auth;

import com.gpadilla.mycar.auth.CurrentUser;
import com.gpadilla.mycar.dtos.auth.LoginRequestDto;
import com.gpadilla.mycar.entity.Usuario;
import com.gpadilla.mycar.enums.UserRole;
import com.gpadilla.mycar.error.BusinessException;
import com.gpadilla.mycar.repository.UsuarioRepository;
import com.gpadilla.mycar.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AccessTokenService accessTokenService;
    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;

    @Transactional
    public Usuario loginOrSignupWithProvider(String providerId, String email, boolean emailVerified) {

        // Buscamos usuario por OAuth2Id, si existe inicia sesión
        Usuario usuario = usuarioRepository.findByProviderIdAndEliminadoFalse(providerId)
                .orElse(null);
        if (usuario != null)
            return usuario;


        // Intentamos registrar al usuario
        
        // Aseguramos que el mail exista
        if (email == null || email.isBlank()) {
            throw new OAuth2AuthenticationException(new OAuth2Error("email_not_provided"),
                    "Email not provided");
        }
        
        // Aseguramos que el mail esté verificado
        if (!emailVerified) {
            throw new OAuth2AuthenticationException(new OAuth2Error("email_not_verified"),
                    "Email is not verified");
        }

        // Intentamos el registro
        try {
            return usuario = usuarioService.registerUserFromAuth0(providerId, email);
        } catch (BusinessException ex) {
            throw new OAuth2AuthenticationException(new OAuth2Error("email_taken"), "Email is already in use");
        }
    }

    public CurrentUser getAuthenticatedUser(LoginRequestDto loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        return (CurrentUser) authentication.getPrincipal();
    }

    @Transactional(readOnly = true)
    public Collection<UserRole> getUserRoles(Long userId) {
        Usuario user = usuarioService.find(userId);
        return List.of(user.getRol());
    }
}
