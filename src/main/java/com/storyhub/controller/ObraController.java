package com.storyhub.controller;

import com.storyhub.dto.request.ObraRequest;
import com.storyhub.dto.response.ObraResponse;
import com.storyhub.service.ObraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/obras")
@RequiredArgsConstructor
public class ObraController {

    private final ObraService obraService;

    @GetMapping
    public ResponseEntity<List<ObraResponse>> listar() {
        return ResponseEntity.ok(obraService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ObraResponse> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(obraService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<ObraResponse> criar(@Valid @RequestBody ObraRequest request) {
        return ResponseEntity.status(201).body(obraService.criar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ObraResponse> atualizar(@PathVariable Integer id,
                                                   @Valid @RequestBody ObraRequest request) {
        return ResponseEntity.ok(obraService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        obraService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}