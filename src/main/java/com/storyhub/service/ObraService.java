package com.storyhub.service;

import com.storyhub.dto.request.ObraRequest;
import com.storyhub.dto.response.ObraResponse;
import com.storyhub.entity.Genero;
import com.storyhub.entity.Obra;
import com.storyhub.exception.ResourceNotFoundException;
import com.storyhub.repository.GeneroRepository;
import com.storyhub.repository.ObraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ObraService {

    private final ObraRepository obraRepository;
    private final GeneroRepository generoRepository;

    public List<ObraResponse> listar() {
        return obraRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ObraResponse buscarPorId(Integer id) {
        Obra obra = obraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Obra não encontrada"));
        return toResponse(obra);
    }

    public ObraResponse criar(ObraRequest request) {
        if (obraRepository.existsByTituloIgnoreCase(request.getTitulo())) {
            throw new RuntimeException("Já existe uma obra com esse título");
        }

        Obra obra = toEntity(request);
        return toResponse(obraRepository.save(obra));
    }

    public ObraResponse atualizar(Integer id, ObraRequest request) {
        Obra obra = obraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Obra não encontrada"));

        obra.setTitulo(request.getTitulo());
        obra.setDescricao(request.getDescricao());
        obra.setTipo(request.getTipo());
        obra.setAutor(request.getAutor());
        obra.setEstudio(request.getEstudio());
        obra.setGeneros(buscarGeneros(request.getGeneroIds()));

        return toResponse(obraRepository.save(obra));
    }

    public void deletar(Integer id) {
        if (!obraRepository.existsById(id)) {
            throw new ResourceNotFoundException("Obra não encontrada");
        }
        obraRepository.deleteById(id);
    }

    // --- Helpers ---

    private Obra toEntity(ObraRequest request) {
        Obra obra = new Obra();
        obra.setTitulo(request.getTitulo());
        obra.setDescricao(request.getDescricao());
        obra.setTipo(request.getTipo());
        obra.setAutor(request.getAutor());
        obra.setEstudio(request.getEstudio());
        obra.setGeneros(buscarGeneros(request.getGeneroIds()));
        return obra;
    }

    private ObraResponse toResponse(Obra obra) {
        ObraResponse response = new ObraResponse();
        response.setIdObra(obra.getIdObra());
        response.setTitulo(obra.getTitulo());
        response.setDescricao(obra.getDescricao());
        response.setTipo(obra.getTipo());
        response.setAutor(obra.getAutor());
        response.setEstudio(obra.getEstudio());
        response.setGeneros(
            obra.getGeneros() == null ? List.of() :
            obra.getGeneros().stream().map(Genero::getNome).toList()
        );
        return response;
    }

    private List<Genero> buscarGeneros(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) return List.of();
        return generoRepository.findAllById(ids);
    }
}