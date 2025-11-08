package com.gpadilla.mycar.service.auth;

import com.gpadilla.mycar.auth.CurrentUser;
import com.gpadilla.mycar.auth.CustomUserDetails;
import com.gpadilla.mycar.dtos.auth.LoginRequestDto;
import com.gpadilla.mycar.dtos.cliente.SignUpFormDto;
import com.gpadilla.mycar.entity.Usuario;
import com.gpadilla.mycar.facade.ClienteFacade;
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

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;
    private final ClienteFacade clienteFacade;

    @Transactional
    public Usuario handleOidcLogin(String providerId, String email, boolean emailVerified) {

        // Buscamos usuario por OAuth2Id, si existe inicia sesión
        Usuario byProvider = usuarioRepository.findByProviderIdAndEliminadoFalse(providerId)
                .orElse(null);
        if (byProvider != null)
            return byProvider;

        // Intentamos buscar por email. Si no está asociado a auth0, lo asociamos e iniciamos sesión
        // Si ya está asociado a otra cuenta de auth0, rechazamos.

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

        Usuario byEmail = usuarioRepository.findByEmailAndEliminadoFalse(email)
                .orElse(null);

        if (byEmail != null) {
            if (byEmail.getProviderId() == null) {
                byEmail.setProviderId(providerId); // asociamos la cuenta a auth0
                return usuarioRepository.save(byEmail);
            } else {
                throw new OAuth2AuthenticationException(new OAuth2Error("email_taken"),
                        "This email is already linked to another provider account.");
            }
        }

        // Como último caso, registramos al usuario
        return usuarioService.createUserFromProvider(providerId, email);
    }

    public CustomUserDetails loginWithEmailPassword(LoginRequestDto loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        return (CustomUserDetails) authentication.getPrincipal();
    }

    public CurrentUser registerUserWithEmailAndPassword(SignUpFormDto signUpFormDto) {
        Usuario usuario = clienteFacade.registrarClientePorFormularioRegistro(signUpFormDto);
        return new CurrentUser(usuario.getId(), List.of(usuario.getRol()));
    }
}
