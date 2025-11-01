package com.gpadilla.mycar.service;

import com.gpadilla.mycar.auth.CustomUserDetails;
import com.gpadilla.mycar.entity.Usuario;
import com.gpadilla.mycar.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmailAndEliminadoFalse(username);

        if (usuarioOpt.isEmpty())
            throw new UsernameNotFoundException("Usuario no encontrado");

        Usuario usuario = usuarioOpt.get();
        return new CustomUserDetails(usuario.getId(), usuario.getEmail(), usuario.getPassword(), usuario.getRol());
    }
}