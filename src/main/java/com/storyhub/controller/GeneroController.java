package com.storyhub.controller;

import com.storyhub.dto.request.GeneroRequest;
import com.storyhub.dto.response.GeneroResponse;
import com.storyhub.service.GeneroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/generos")
@RequiredArgsConstructor
public class GeneroController {

    private final GeneroService generoService;

    @GetMapping
    public ResponseEntity<List<GeneroResponse>> listar() {
        return ResponseEntity.ok(generoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneroResponse> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(generoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<GeneroResponse> criar(@Valid @RequestBody GeneroRequest request) {
        return ResponseEntity.status(201).body(generoService.criar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GeneroResponse> atualizar(@PathVariable Integer id,
                                                     @Valid @RequestBody GeneroRequest request) {
        return ResponseEntity.ok(generoService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        generoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}