package com.storyhub.service;

import com.storyhub.dto.request.ColecaoUsuarioRequest;
import com.storyhub.dto.response.ColecaoUsuarioResponse;
import com.storyhub.entity.ColecaoUsuario;
import com.storyhub.entity.Usuario;
import com.storyhub.entity.Volume;
import com.storyhub.exception.ResourceNotFoundException;
import com.storyhub.repository.ColecaoUsuarioRepository;
import com.storyhub.repository.VolumeRepository;
import com.storyhub.security.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ColecaoUsuarioService {

    private final ColecaoUsuarioRepository colecaoRepository;
    private final VolumeRepository volumeRepository;
    private final AuthUtil authUtil;

    public List<ColecaoUsuarioResponse> listar() {
        Usuario usuario = authUtil.getUsuarioAutenticado();
        return colecaoRepository.findByUsuario_IdUsuario(usuario.getIdUsuario())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ColecaoUsuarioResponse buscarPorId(Integer id) {
        ColecaoUsuario colecao = buscarColecaoDoUsuario(id);
        return toResponse(colecao);
    }

    public ColecaoUsuarioResponse criar(ColecaoUsuarioRequest request) {
        Usuario usuario = authUtil.getUsuarioAutenticado();

        if (colecaoRepository.existsByUsuario_IdUsuarioAndVolume_IdVolume(
                usuario.getIdUsuario(), request.getVolumeId())) {
            throw new RuntimeException("Volume já adicionado na coleção");
        }

        Volume volume = volumeRepository.findById(request.getVolumeId())
                .orElseThrow(() -> new ResourceNotFoundException("Volume não encontrado"));

        ColecaoUsuario colecao = new ColecaoUsuario();
        colecao.setUsuario(usuario);
        colecao.setVolume(volume);
        colecao.setPossui(request.getPossui());
        colecao.setLido(request.getLido());
        colecao.setDataCompra(request.getDataCompra());
        colecao.setValorPago(request.getValorPago());

        return toResponse(colecaoRepository.save(colecao));
    }

    public ColecaoUsuarioResponse atualizar(Integer id, ColecaoUsuarioRequest request) {
        ColecaoUsuario colecao = buscarColecaoDoUsuario(id);

        Volume volume = volumeRepository.findById(request.getVolumeId())
                .orElseThrow(() -> new ResourceNotFoundException("Volume não encontrado"));

        colecao.setVolume(volume);
        colecao.setPossui(request.getPossui());
        colecao.setLido(request.getLido());
        colecao.setDataCompra(request.getDataCompra());
        colecao.setValorPago(request.getValorPago());

        return toResponse(colecaoRepository.save(colecao));
    }

    public void deletar(Integer id) {
        ColecaoUsuario colecao = buscarColecaoDoUsuario(id);
        colecaoRepository.delete(colecao);
    }

    // --- Helpers ---

    private ColecaoUsuario buscarColecaoDoUsuario(Integer id) {
        Usuario usuario = authUtil.getUsuarioAutenticado();

        ColecaoUsuario colecao = colecaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item não encontrado na coleção"));

        if (!colecao.getUsuario().getIdUsuario().equals(usuario.getIdUsuario())) {
            throw new RuntimeException("Acesso negado");
        }

        return colecao;
    }

    private ColecaoUsuarioResponse toResponse(ColecaoUsuario colecao) {
        ColecaoUsuarioResponse response = new ColecaoUsuarioResponse();
        response.setIdColecao(colecao.getIdColecao());
        response.setVolumeId(colecao.getVolume().getIdVolume());
        response.setTituloObra(colecao.getVolume().getObra().getTitulo());
        response.setNumeroVolume(colecao.getVolume().getNumeroVolume());
        response.setImagemUrl(colecao.getVolume().getObra().getImagemUrl());
        response.setPossui(colecao.getPossui());
        response.setLido(colecao.getLido());
        response.setDataCompra(colecao.getDataCompra());
        response.setValorPago(colecao.getValorPago());
        return response;
    }
}