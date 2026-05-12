package com.storyhub.service;

import com.storyhub.dto.request.HistoricoRequest;
import com.storyhub.dto.response.HistoricoResponse;
import com.storyhub.entity.Biblioteca;
import com.storyhub.entity.Historico;
import com.storyhub.entity.Usuario;
import com.storyhub.exception.ResourceNotFoundException;
import com.storyhub.repository.BibliotecaRepository;
import com.storyhub.repository.HistoricoRepository;
import com.storyhub.security.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoricoService {

    private final HistoricoRepository historicoRepository;
    private final BibliotecaRepository bibliotecaRepository;
    private final AuthUtil authUtil;

    public List<HistoricoResponse> listarPorBiblioteca(Integer bibliotecaId) {
        validarDonoBiblioteca(bibliotecaId);
        return historicoRepository
                .findByBiblioteca_IdBibliotecaOrderByDataRegistroDesc(bibliotecaId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public HistoricoResponse registrar(HistoricoRequest request) {
        Biblioteca biblioteca = validarDonoBiblioteca(request.getBibliotecaId());

        Historico historico = new Historico();
        historico.setBiblioteca(biblioteca);
        historico.setCapitulo(request.getCapitulo());
        historico.setDataRegistro(LocalDateTime.now());

        return toResponse(historicoRepository.save(historico));
    }

    public void deletar(Integer id) {
        Historico historico = historicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registro não encontrado"));

        validarDonoBiblioteca(historico.getBiblioteca().getIdBiblioteca());

        historicoRepository.delete(historico);
    }

    // --- Helpers ---

    private Biblioteca validarDonoBiblioteca(Integer bibliotecaId) {
        Usuario usuario = authUtil.getUsuarioAutenticado();

        Biblioteca biblioteca = bibliotecaRepository.findById(bibliotecaId)
                .orElseThrow(() -> new ResourceNotFoundException("Biblioteca não encontrada"));

        if (!biblioteca.getUsuario().getIdUsuario().equals(usuario.getIdUsuario())) {
            throw new RuntimeException("Acesso negado");
        }

        return biblioteca;
    }

    private HistoricoResponse toResponse(Historico historico) {
        HistoricoResponse response = new HistoricoResponse();
        response.setIdHistorico(historico.getIdHistorico());
        response.setIdHistorico(historico.getIdHistorico());
        response.setBibliotecaId(historico.getBiblioteca().getIdBiblioteca());
        response.setTituloObra(historico.getBiblioteca().getObra().getTitulo());
        response.setCapitulo(historico.getCapitulo());
        response.setDataRegistro(historico.getDataRegistro());
        return response;
    }
}