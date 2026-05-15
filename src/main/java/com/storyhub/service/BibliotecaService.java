package com.storyhub.service;

import com.storyhub.dto.request.BibliotecaRequest;
import com.storyhub.dto.response.BibliotecaResponse;
import com.storyhub.entity.Biblioteca;
import com.storyhub.entity.Obra;
import com.storyhub.entity.Usuario;
import com.storyhub.enums.StatusBiblioteca;
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

    private StatusBiblioteca calcularStatus(Biblioteca biblioteca, StatusBiblioteca statusSolicitado) {
        // ABANDONADO e PLANEJO_VER são sempre respeitados
        if (statusSolicitado == StatusBiblioteca.ABANDONADO) return StatusBiblioteca.ABANDONADO;
        if (statusSolicitado == StatusBiblioteca.PLANEJO_VER) return StatusBiblioteca.PLANEJO_VER;

        int progresso    = biblioteca.getProgressoAtual();
        int total        = biblioteca.getObra().getTotalUnidade();
        boolean emLancamento = Boolean.TRUE.equals(biblioteca.getEmLancamento());

        // COMPLETO — verifica se faz sentido
        if (statusSolicitado == StatusBiblioteca.COMPLETO) {
            // se obra ainda em lançamento, não pode marcar como completo
            if (emLancamento) return StatusBiblioteca.ACOMPANHANDO;
            return StatusBiblioteca.COMPLETO;
        }

        // ACOMPANHANDO — verifica se chegou ao fim
        if (total > 0 && progresso >= total && !emLancamento) {
            return StatusBiblioteca.COMPLETO;
        }

        return StatusBiblioteca.ACOMPANHANDO;
    }

    public BibliotecaResponse criar(BibliotecaRequest request) {
        Usuario usuario = authUtil.getUsuarioAutenticado();

        if (bibliotecaRepository.existsByUsuario_IdUsuarioAndObra_IdObra(
                usuario.getIdUsuario(), request.getObraId())) {
            throw new RuntimeException("Obra já adicionada na biblioteca");
        }

        Obra obra = obraRepository.findById(request.getObraId())
                .orElseThrow(() -> new ResourceNotFoundException("Obra não encontrada"));

        int totalUnidade = obra.getTotalUnidade();
        if (totalUnidade > 0 && request.getProgressoAtual() > totalUnidade) {
            throw new RuntimeException("Progresso não pode ser maior que o total da obra (" + totalUnidade + ")");
        }

        Biblioteca biblioteca = new Biblioteca();
        biblioteca.setUsuario(usuario);
        biblioteca.setObra(obra);
        biblioteca.setEmLancamento(request.getEmLancamento());
        biblioteca.setProgressoAtual(request.getProgressoAtual());
        biblioteca.setFavorito(request.getFavorito());
        biblioteca.setNota(request.getNota());
        biblioteca.setStatus(calcularStatus(biblioteca, request.getStatus()));

        return toResponse(bibliotecaRepository.save(biblioteca));
    }

    public BibliotecaResponse atualizar(Integer id, BibliotecaRequest request) {
        Biblioteca biblioteca = buscarBibliotecaDoUsuario(id);

        Obra obra = obraRepository.findById(request.getObraId())
                .orElseThrow(() -> new ResourceNotFoundException("Obra não encontrada"));

        int totalUnidade = obra.getTotalUnidade();
        if (totalUnidade > 0 && request.getProgressoAtual() > totalUnidade) {
            throw new RuntimeException("Progresso não pode ser maior que o total da obra (" + totalUnidade + ")");
        }

        biblioteca.setObra(obra);
        biblioteca.setEmLancamento(request.getEmLancamento());
        biblioteca.setFavorito(request.getFavorito());
        biblioteca.setNota(request.getNota());

        if (request.getStatus() == StatusBiblioteca.COMPLETO) {
            biblioteca.setProgressoAtual(totalUnidade);
        } else {
            biblioteca.setProgressoAtual(request.getProgressoAtual());
        }

        biblioteca.setStatus(calcularStatus(biblioteca, request.getStatus()));

        return toResponse(bibliotecaRepository.save(biblioteca));
    }

    public void deletar(Integer id) {
        Biblioteca biblioteca = buscarBibliotecaDoUsuario(id);
        bibliotecaRepository.delete(biblioteca);
    }

    public BibliotecaResponse incrementarProgresso(Integer id) {
        Biblioteca biblioteca = buscarBibliotecaDoUsuario(id);

        if (biblioteca.getStatus() == StatusBiblioteca.COMPLETO) {
            throw new RuntimeException("Obra já está completa");
        }
        if (biblioteca.getStatus() == StatusBiblioteca.ABANDONADO) {
            throw new RuntimeException("Obra está abandonada");
        }

        biblioteca.setProgressoAtual(biblioteca.getProgressoAtual() + 1);
        biblioteca.setStatus(calcularStatus(biblioteca, StatusBiblioteca.ACOMPANHANDO));

        return toResponse(bibliotecaRepository.save(biblioteca));
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
        response.setTotalUnidade(biblioteca.getObra().getTotalUnidade());
        response.setEmLancamento(biblioteca.getEmLancamento());
        response.setFavorito(biblioteca.getFavorito());
        response.setNota(biblioteca.getNota());
        response.setGeneros(biblioteca.getObra().getGeneros() == null ? List.of() :
                biblioteca.getObra().getGeneros());
        return response;
    }
}