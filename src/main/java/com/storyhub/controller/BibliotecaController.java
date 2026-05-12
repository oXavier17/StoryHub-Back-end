package com.storyhub.controller;

import com.storyhub.dto.request.BibliotecaRequest;
import com.storyhub.dto.response.BibliotecaResponse;
import com.storyhub.service.BibliotecaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/biblioteca")
@RequiredArgsConstructor
public class BibliotecaController {

    private final BibliotecaService bibliotecaService;

    @GetMapping
    public ResponseEntity<List<BibliotecaResponse>> listar() {
        return ResponseEntity.ok(bibliotecaService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BibliotecaResponse> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(bibliotecaService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<BibliotecaResponse> criar(@Valid @RequestBody BibliotecaRequest request) {
        return ResponseEntity.status(201).body(bibliotecaService.criar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BibliotecaResponse> atualizar(@PathVariable Integer id,
                                                         @Valid @RequestBody BibliotecaRequest request) {
        return ResponseEntity.ok(bibliotecaService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        bibliotecaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}