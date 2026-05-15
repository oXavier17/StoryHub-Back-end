package com.storyhub.service;

import com.storyhub.dto.response.BibliotecaResponse;
import com.storyhub.dto.response.DashboardResponse;
import com.storyhub.entity.Usuario;
import com.storyhub.enums.StatusBiblioteca;
import com.storyhub.repository.BibliotecaRepository;
import com.storyhub.security.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final BibliotecaRepository bibliotecaRepository;
    private final AuthUtil authUtil;

    public DashboardResponse getDashboard() {
        Usuario usuario = authUtil.getUsuarioAutenticado();
        Integer id = usuario.getIdUsuario();

        long totalObras     = bibliotecaRepository.countByUsuario_IdUsuario(id);
        long totalLendo     = bibliotecaRepository.countByUsuario_IdUsuarioAndStatus(id, StatusBiblioteca.ACOMPANHANDO);
        long totalCompletos = bibliotecaRepository.countByUsuario_IdUsuarioAndStatus(id, StatusBiblioteca.COMPLETO);
        long totalFavoritos = bibliotecaRepository.countByUsuario_IdUsuarioAndFavoritoTrue(id);

        List<BibliotecaResponse> ultimas = bibliotecaRepository
                .findTop5ByUsuario_IdUsuarioOrderByIdBibliotecaDesc(id)
                .stream()
                .map(this::toResponse)
                .toList();

        return new DashboardResponse(totalObras, totalLendo, totalCompletos, totalFavoritos, ultimas);
    }

    private BibliotecaResponse toResponse(com.storyhub.entity.Biblioteca biblioteca) {
        BibliotecaResponse response = new BibliotecaResponse();
        response.setIdBiblioteca(biblioteca.getIdBiblioteca());
        response.setObraId(biblioteca.getObra().getIdObra());
        response.setTituloObra(biblioteca.getObra().getTitulo());
        response.setTipoObra(biblioteca.getObra().getTipo().name());
        response.setImagemUrl(biblioteca.getObra().getImagemUrl());
        response.setStatus(biblioteca.getStatus());
        response.setProgressoAtual(biblioteca.getProgressoAtual());
        response.setFavorito(biblioteca.getFavorito());
        return response;
    }
}