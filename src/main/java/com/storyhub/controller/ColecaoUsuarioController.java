package com.storyhub.controller;

import com.storyhub.dto.request.ColecaoUsuarioRequest;
import com.storyhub.dto.response.ColecaoUsuarioResponse;
import com.storyhub.service.ColecaoUsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/colecao")
@RequiredArgsConstructor
public class ColecaoUsuarioController {

    private final ColecaoUsuarioService colecaoService;

    @GetMapping
    public ResponseEntity<List<ColecaoUsuarioResponse>> listar() {
        return ResponseEntity.ok(colecaoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ColecaoUsuarioResponse> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(colecaoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<ColecaoUsuarioResponse> criar(@Valid @RequestBody ColecaoUsuarioRequest request) {
        return ResponseEntity.status(201).body(colecaoService.criar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ColecaoUsuarioResponse> atualizar(@PathVariable Integer id,
                                                             @Valid @RequestBody ColecaoUsuarioRequest request) {
        return ResponseEntity.ok(colecaoService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        colecaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}