package com.storyhub.security;

import com.storyhub.entity.Usuario;
import com.storyhub.exception.ResourceNotFoundException;
import com.storyhub.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthUtil {

    private final UsuarioRepository usuarioRepository;

    public Usuario getUsuarioAutenticado() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
    }
}