package com.storyhub.controller;

import com.storyhub.dto.request.HistoricoRequest;
import com.storyhub.dto.response.HistoricoResponse;
import com.storyhub.service.HistoricoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/historico")
@RequiredArgsConstructor
public class HistoricoController {

    private final HistoricoService historicoService;

    @GetMapping("/biblioteca/{bibliotecaId}")
    public ResponseEntity<List<HistoricoResponse>> listar(@PathVariable Integer bibliotecaId) {
        return ResponseEntity.ok(historicoService.listarPorBiblioteca(bibliotecaId));
    }

    @PostMapping
    public ResponseEntity<HistoricoResponse> registrar(@Valid @RequestBody HistoricoRequest request) {
        return ResponseEntity.status(201).body(historicoService.registrar(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        historicoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}