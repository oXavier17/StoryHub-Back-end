package com.storyhub.service;

import com.storyhub.dto.request.LoginRequest;
import com.storyhub.dto.request.RegistroRequest;
import com.storyhub.dto.response.LoginResponse;
import com.storyhub.entity.Usuario;
import com.storyhub.repository.UsuarioRepository;
import com.storyhub.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha())
        );

        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        String token = jwtUtil.gerarToken(usuario.getEmail());

        return new LoginResponse(token, usuario.getNome(), usuario.getEmail());
    }

    public LoginResponse registro(RegistroRequest request) {
        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email já cadastrado");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());
        usuario.setSenha(passwordEncoder.encode(request.getSenha()));

        usuarioRepository.save(usuario);

        String token = jwtUtil.gerarToken(usuario.getEmail());

        return new LoginResponse(token, usuario.getNome(), usuario.getEmail());
    }
}