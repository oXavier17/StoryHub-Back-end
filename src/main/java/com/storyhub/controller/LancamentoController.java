package com.storyhub.controller;

import com.storyhub.dto.request.LancamentoRequest;
import com.storyhub.dto.response.LancamentoResponse;
import com.storyhub.service.LancamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lancamentos")
@RequiredArgsConstructor
public class LancamentoController {

    private final LancamentoService lancamentoService;

    @GetMapping
    public ResponseEntity<List<LancamentoResponse>> listar() {
        return ResponseEntity.ok(lancamentoService.listar());
    }

    @GetMapping("/obra/{obraId}")
    public ResponseEntity<LancamentoResponse> buscarPorObra(@PathVariable Integer obraId) {
        return ResponseEntity.ok(lancamentoService.buscarPorObra(obraId));
    }

    @PostMapping
    public ResponseEntity<LancamentoResponse> criar(@Valid @RequestBody LancamentoRequest request) {
        return ResponseEntity.status(201).body(lancamentoService.criar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LancamentoResponse> atualizar(@PathVariable Integer id,
                                                         @Valid @RequestBody LancamentoRequest request) {
        return ResponseEntity.ok(lancamentoService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        lancamentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}