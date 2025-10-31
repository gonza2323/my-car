package com.gpadilla.mycar.service;

import com.gpadilla.mycar.auth.CustomUserDetails;
import com.gpadilla.mycar.entity.Usuario;
import com.gpadilla.mycar.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmailAndEliminadoFalse(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return CustomUserDetails.builder()
                .id(usuario.getId())
                .email(usuario.getEmail())
                .password(usuario.getPassword())
                .rol(usuario.getRol()).build();
    }
}