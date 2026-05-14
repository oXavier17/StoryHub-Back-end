package com.storyhub.service;

import com.storyhub.dto.request.BibliotecaRequest;
import com.storyhub.dto.response.BibliotecaResponse;
import com.storyhub.entity.Biblioteca;
import com.storyhub.entity.Obra;
import com.storyhub.entity.Usuario;
import com.storyhub.exception.ResourceNotFoundException;
import com.storyhub.repository.BibliotecaRepository;
import com.storyhub.repository.ObraRepository;
import com.storyhub.security.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BibliotecaService {

    private final BibliotecaRepository bibliotecaRepository;
    private final ObraRepository obraRepository;
    private final AuthUtil authUtil;

    public List<BibliotecaResponse> listar() {
        Usuario usuario = authUtil.getUsuarioAutenticado();
        return bibliotecaRepository.findByUsuario_IdUsuario(usuario.getIdUsuario())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public BibliotecaResponse buscarPorId(Integer id) {
        Biblioteca biblioteca = buscarBibliotecaDoUsuario(id);
        return toResponse(biblioteca);
    }

    public BibliotecaResponse criar(BibliotecaRequest request) {
        Usuario usuario = authUtil.getUsuarioAutenticado();

        if (bibliotecaRepository.existsByUsuario_IdUsuarioAndObra_IdObra(
                usuario.getIdUsuario(), request.getObraId())) {
            throw new RuntimeException("Obra já adicionada na biblioteca");
        }

        Obra obra = obraRepository.findById(request.getObraId())
                .orElseThrow(() -> new ResourceNotFoundException("Obra não encontrada"));

        Biblioteca biblioteca = new Biblioteca();
        biblioteca.setUsuario(usuario);
        biblioteca.setObra(obra);
        biblioteca.setStatus(request.getStatus());
        biblioteca.setProgressoAtual(request.getProgressoAtual());
        biblioteca.setTotalUnidade(request.getTotalUnidade());
        biblioteca.setFavorito(request.getFavorito());

        return toResponse(bibliotecaRepository.save(biblioteca));
    }

    public BibliotecaResponse atualizar(Integer id, BibliotecaRequest request) {
        Biblioteca biblioteca = buscarBibliotecaDoUsuario(id);

        Obra obra = obraRepository.findById(request.getObraId())
                .orElseThrow(() -> new ResourceNotFoundException("Obra não encontrada"));

        biblioteca.setObra(obra);
        biblioteca.setStatus(request.getStatus());
        biblioteca.setProgressoAtual(request.getProgressoAtual());
        biblioteca.setTotalUnidade(request.getTotalUnidade());
        biblioteca.setFavorito(request.getFavorito());

        return toResponse(bibliotecaRepository.save(biblioteca));
    }

    public void deletar(Integer id) {
        Biblioteca biblioteca = buscarBibliotecaDoUsuario(id);
        bibliotecaRepository.delete(biblioteca);
    }

    // --- Helpers ---

    private Biblioteca buscarBibliotecaDoUsuario(Integer id) {
        Usuario usuario = authUtil.getUsuarioAutenticado();
        Biblioteca biblioteca = bibliotecaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entrada não encontrada na biblioteca"));

        if (!biblioteca.getUsuario().getIdUsuario().equals(usuario.getIdUsuario())) {
            throw new RuntimeException("Acesso negado");
        }

        return biblioteca;
    }

    private BibliotecaResponse toResponse(Biblioteca biblioteca) {
        BibliotecaResponse response = new BibliotecaResponse();
        response.setIdBiblioteca(biblioteca.getIdBiblioteca());
        response.setObraId(biblioteca.getObra().getIdObra());
        response.setTituloObra(biblioteca.getObra().getTitulo());
        response.setTipoObra(biblioteca.getObra().getTipo().name());
        response.setImagemUrl(biblioteca.getObra().getImagemUrl());
        response.setStatus(biblioteca.getStatus());
        response.setProgressoAtual(biblioteca.getProgressoAtual());
        response.setTotalUnidade(biblioteca.getTotalUnidade());
        response.setFavorito(biblioteca.getFavorito());
        response.setGeneros(
            biblioteca.getObra().getGeneros() == null ? List.of() :
            biblioteca.getObra().getGeneros()
        );
        return response;
    }
}