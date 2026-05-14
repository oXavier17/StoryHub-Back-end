package com.storyhub.controller;

import com.storyhub.dto.request.CriarVolumesLoteRequest;
import com.storyhub.dto.request.VolumeRequest;
import com.storyhub.dto.response.VolumeResponse;
import com.storyhub.service.VolumeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/volumes")
@RequiredArgsConstructor
public class VolumeController {

    private final VolumeService volumeService;

    @GetMapping("/obra/{obraId}")
    public ResponseEntity<List<VolumeResponse>> listarPorObra(@PathVariable Integer obraId) {
        return ResponseEntity.ok(volumeService.listarPorObra(obraId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VolumeResponse> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(volumeService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<VolumeResponse> criar(@Valid @RequestBody VolumeRequest request) {
        return ResponseEntity.status(201).body(volumeService.criar(request));
    }

    @PostMapping("/lote")
    public ResponseEntity<List<VolumeResponse>> criarLote(@RequestBody CriarVolumesLoteRequest request) {
        return ResponseEntity.status(201).body(volumeService.criarLote(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VolumeResponse> atualizar(@PathVariable Integer id,
                                                     @Valid @RequestBody VolumeRequest request) {
        return ResponseEntity.ok(volumeService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        volumeService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}