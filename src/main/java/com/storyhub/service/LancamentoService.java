package com.storyhub.service;

import com.storyhub.dto.request.LancamentoRequest;
import com.storyhub.dto.response.LancamentoResponse;
import com.storyhub.entity.Lancamento;
import com.storyhub.entity.Obra;
import com.storyhub.entity.Usuario;
import com.storyhub.enums.StatusBiblioteca;
import com.storyhub.exception.ResourceNotFoundException;
import com.storyhub.repository.LancamentoRepository;
import com.storyhub.repository.ObraRepository;
import com.storyhub.security.AuthUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LancamentoService {

    private final LancamentoRepository lancamentoRepository;
    private final ObraRepository obraRepository;
    private final AuthUtil authUtil;

    public List<LancamentoResponse> listar() {
        return lancamentoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public LancamentoResponse buscarPorObra(Integer obraId) {
        Lancamento lancamento = lancamentoRepository.findByObra_IdObra(obraId)
                .orElseThrow(() -> new ResourceNotFoundException("Lançamento não encontrado para essa obra"));
        return toResponse(lancamento);
    }

    public LancamentoResponse criar(LancamentoRequest request) {
        if (lancamentoRepository.existsByObra_IdObra(request.getObraId())) {
            Lancamento existente = lancamentoRepository.findByObra_IdObra(request.getObraId())
                    .orElseThrow(() -> new ResourceNotFoundException("Lançamento não encontrado"));
            existente.setFrequencia(request.getFrequencia());
            existente.setDiaSemana(request.getDiaSemana());
            existente.setDiaMes(request.getDiaMes());
            existente.setHorarioLancamento(request.getHorarioLancamento());
            return toResponse(lancamentoRepository.save(existente));
        }

        Obra obra = obraRepository.findById(request.getObraId())
                .orElseThrow(() -> new ResourceNotFoundException("Obra não encontrada"));

        Lancamento lancamento = new Lancamento();
        lancamento.setObra(obra);
        lancamento.setFrequencia(request.getFrequencia());
        lancamento.setDiaSemana(request.getDiaSemana());
        lancamento.setDiaMes(request.getDiaMes());
        lancamento.setHorarioLancamento(request.getHorarioLancamento());

        return toResponse(lancamentoRepository.save(lancamento));
    }

    public LancamentoResponse atualizar(Integer id, LancamentoRequest request) {
        Lancamento lancamento = lancamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lançamento não encontrado"));

        Obra obra = obraRepository.findById(request.getObraId())
                .orElseThrow(() -> new ResourceNotFoundException("Obra não encontrada"));

        lancamento.setObra(obra);
        lancamento.setFrequencia(request.getFrequencia());
        lancamento.setDiaSemana(request.getDiaSemana());
        lancamento.setDiaMes(request.getDiaMes());
        lancamento.setHorarioLancamento(request.getHorarioLancamento());

        return toResponse(lancamentoRepository.save(lancamento));
    }

    public List<LancamentoResponse> listarDoUsuario() {
        Usuario usuario = authUtil.getUsuarioAutenticado();
        return lancamentoRepository
                .findByUsuarioAcompanhando(usuario.getIdUsuario(), StatusBiblioteca.ACOMPANHANDO)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public void deletar(Integer id) {
        if (!lancamentoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Lançamento não encontrado");
        }
        lancamentoRepository.deleteById(id);
    }

    // --- Helpers ---

    private LancamentoResponse toResponse(Lancamento lancamento) {
        LancamentoResponse response = new LancamentoResponse();
        response.setIdLancamento(lancamento.getIdLancamento());
        response.setObraId(lancamento.getObra().getIdObra());
        response.setTituloObra(lancamento.getObra().getTitulo());
        response.setImagemUrl(lancamento.getObra().getImagemUrl());
        response.setTipoObra(lancamento.getObra().getTipo().name());
        response.setFrequencia(lancamento.getFrequencia());
        response.setDiaSemana(lancamento.getDiaSemana());
        response.setDiaMes(lancamento.getDiaMes());
        response.setHorarioLancamento(lancamento.getHorarioLancamento());
        return response;
    }
}