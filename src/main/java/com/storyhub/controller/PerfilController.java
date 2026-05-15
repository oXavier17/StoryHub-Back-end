package com.storyhub.controller;

import com.storyhub.dto.request.AtualizarPerfilRequest;
import com.storyhub.dto.request.TrocarSenhaRequest;
import com.storyhub.dto.response.UsuarioResponse;
import com.storyhub.service.PerfilService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/perfil")
@RequiredArgsConstructor
public class PerfilController {

    private final PerfilService perfilService;

    @GetMapping
    public ResponseEntity<UsuarioResponse> getPerfil() {
        return ResponseEntity.ok(perfilService.getPerfil());
    }

    @PutMapping
    public ResponseEntity<UsuarioResponse> atualizar(@Valid @RequestBody AtualizarPerfilRequest request) {
        return ResponseEntity.ok(perfilService.atualizarPerfil(request));
    }

    @PutMapping("/senha")
    public ResponseEntity<Void> trocarSenha(@Valid @RequestBody TrocarSenhaRequest request) {
        perfilService.trocarSenha(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/foto")
    public ResponseEntity<UsuarioResponse> uploadFoto(@RequestParam("arquivo") MultipartFile arquivo) {
        return ResponseEntity.ok(perfilService.atualizarFoto(arquivo));
    }

    @PutMapping("/foto-url")
    public ResponseEntity<UsuarioResponse> atualizarFotoUrl(@RequestBody java.util.Map<String, String> body) {
        return ResponseEntity.ok(perfilService.atualizarFotoUrl(body.get("url")));
    }
}