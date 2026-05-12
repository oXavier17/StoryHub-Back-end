package com.storyhub.service;

import com.storyhub.dto.request.GeneroRequest;
import com.storyhub.dto.response.GeneroResponse;
import com.storyhub.entity.Genero;
import com.storyhub.exception.ResourceNotFoundException;
import com.storyhub.repository.GeneroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GeneroService {

    private final GeneroRepository generoRepository;

    public List<GeneroResponse> listar() {
        return generoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public GeneroResponse buscarPorId(Integer id) {
        Genero genero = generoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Gênero não encontrado"));
        return toResponse(genero);
    }

    public GeneroResponse criar(GeneroRequest request) {
        if (generoRepository.existsByNomeIgnoreCase(request.getNome())) {
            throw new RuntimeException("Gênero já cadastrado");
        }

        Genero genero = new Genero();
        genero.setNome(request.getNome());

        return toResponse(generoRepository.save(genero));
    }

    public GeneroResponse atualizar(Integer id, GeneroRequest request) {
        Genero genero = generoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Gênero não encontrado"));

        genero.setNome(request.getNome());

        return toResponse(generoRepository.save(genero));
    }

    public void deletar(Integer id) {
        if (!generoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Gênero não encontrado");
        }
        generoRepository.deleteById(id);
    }

    // --- Helpers ---

    private GeneroResponse toResponse(Genero genero) {
        GeneroResponse response = new GeneroResponse();
        response.setIdGenero(genero.getIdGenero());
        response.setNome(genero.getNome());
        return response;
    }
}