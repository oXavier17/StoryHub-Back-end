package com.storyhub.controller;

import com.storyhub.dto.request.ObraRequest;
import com.storyhub.dto.response.ObraResponse;
import com.storyhub.enums.Genero;
import com.storyhub.service.ObraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @GetMapping("/generos")
    public ResponseEntity<List<Genero>> listarGeneros() {
        return ResponseEntity.ok(List.of(Genero.values()));
    }

    @PostMapping
    public ResponseEntity<ObraResponse> criar(@Valid @RequestBody ObraRequest request) {
        return ResponseEntity.status(201).body(obraService.criar(request));
    }

    @PostMapping("/{id}/imagem")
    public ResponseEntity<ObraResponse> uploadImagem(@PathVariable Integer id,
                                                    @RequestParam("arquivo") MultipartFile arquivo) {
        return ResponseEntity.ok(obraService.uploadImagem(id, arquivo));
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