package com.storyhub.service;

import com.storyhub.dto.request.AtualizarPerfilRequest;
import com.storyhub.dto.request.TrocarSenhaRequest;
import com.storyhub.dto.response.UsuarioResponse;
import com.storyhub.entity.Usuario;
import com.storyhub.repository.UsuarioRepository;
import com.storyhub.security.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PerfilService {

    private final UsuarioRepository usuarioRepository;
    private final AuthUtil authUtil;
    private final PasswordEncoder passwordEncoder;

    @Value("${upload.dir}")
    private String uploadDir;

    public UsuarioResponse getPerfil() {
        Usuario usuario = authUtil.getUsuarioAutenticado();
        return toResponse(usuario);
    }

    public UsuarioResponse atualizarPerfil(AtualizarPerfilRequest request) {
        Usuario usuario = authUtil.getUsuarioAutenticado();

        // verifica se o email já está em uso por outro usuário
        if (!usuario.getEmail().equals(request.getEmail())) {
            usuarioRepository.findByEmail(request.getEmail()).ifPresent(u -> {
                throw new RuntimeException("Email já está em uso");
            });
        }

        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());

        return toResponse(usuarioRepository.save(usuario));
    }

    public void trocarSenha(TrocarSenhaRequest request) {
        Usuario usuario = authUtil.getUsuarioAutenticado();

        if (!passwordEncoder.matches(request.getSenhaAtual(), usuario.getSenha())) {
            throw new RuntimeException("Senha atual incorreta");
        }

        usuario.setSenha(passwordEncoder.encode(request.getNovaSenha()));
        usuarioRepository.save(usuario);
    }

    public UsuarioResponse atualizarFoto(MultipartFile arquivo) {
        Usuario usuario = authUtil.getUsuarioAutenticado();

        try {
            Path pasta = Paths.get(uploadDir + "/perfil");
            if (!Files.exists(pasta)) Files.createDirectories(pasta);

            String nomeArquivo = UUID.randomUUID() + "_" + arquivo.getOriginalFilename();
            Path destino = pasta.resolve(nomeArquivo);
            Files.copy(arquivo.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);

            usuario.setFotoPerfil("/" + uploadDir + "/perfil/" + nomeArquivo);
            return toResponse(usuarioRepository.save(usuario));
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar foto");
        }
    }

    public UsuarioResponse atualizarFotoUrl(String url) {
        Usuario usuario = authUtil.getUsuarioAutenticado();
        usuario.setFotoPerfil(url);
        return toResponse(usuarioRepository.save(usuario));
    }

    private UsuarioResponse toResponse(Usuario usuario) {
        UsuarioResponse response = new UsuarioResponse();
        response.setIdUsuario(usuario.getIdUsuario());
        response.setNome(usuario.getNome());
        response.setEmail(usuario.getEmail());
        response.setFotoPerfil(usuario.getFotoPerfil());
        return response;
    }
}