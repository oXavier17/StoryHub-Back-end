package com.storyhub.service;

import com.storyhub.dto.request.ObraRequest;
import com.storyhub.dto.response.ObraResponse;
import com.storyhub.entity.Biblioteca;
import com.storyhub.entity.Obra;
import com.storyhub.entity.Usuario;
import com.storyhub.enums.StatusBiblioteca;
import com.storyhub.exception.ResourceNotFoundException;
import com.storyhub.repository.BibliotecaRepository;
import com.storyhub.repository.ObraRepository;
import com.storyhub.security.AuthUtil;
import org.springframework.transaction.annotation.Transactional;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.beans.factory.annotation.Value;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ObraService {

    private final ObraRepository obraRepository;
    private final AuthUtil authUtil;
    private final BibliotecaRepository bibliotecaRepository;

    @Value("${upload.dir}")
    private String uploadDir;

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
        Usuario usuario = authUtil.getUsuarioAutenticado();

        if (obraRepository.existsByTituloIgnoreCaseAndTipo(
                request.getTitulo(), request.getTipo())) {
            throw new RuntimeException("Já existe uma obra com esse título e tipo");
        }

        Obra obra = toEntity(request);
        obra.setUsuario(usuario);

        return toResponse(obraRepository.save(obra));
    }

    @Transactional
    public ObraResponse atualizar(Integer id, ObraRequest request) {
        Obra obra = buscarObraDoUsuario(id);

        Integer totalAnterior = obra.getTotalUnidade();

        obra.setTitulo(request.getTitulo());
        obra.setDescricao(request.getDescricao());
        obra.setTipo(request.getTipo());
        obra.setImagemUrl(request.getImagemUrl());
        obra.setAutor(request.getAutor());
        obra.setEstudio(request.getEstudio());
        obra.setTotalUnidade(request.getTotalUnidade());
        obra.setGeneros(request.getGeneros() != null
                ? new ArrayList<>(request.getGeneros()) : new ArrayList<>());

        obra = obraRepository.save(obra);

        // se o total mudou, recalcula status das bibliotecas afetadas
        if (!Objects.equals(totalAnterior, request.getTotalUnidade())) {
            atualizarStatusBibliotecas(obra);
        }

        return toResponse(obra);
    }

    private void atualizarStatusBibliotecas(Obra obra) {
        List<Biblioteca> bibliotecas = bibliotecaRepository.findByObra_IdObra(obra.getIdObra());
        for (Biblioteca bib : bibliotecas) {
            if (bib.getStatus() == StatusBiblioteca.ABANDONADO) continue;

            int total    = obra.getTotalUnidade();
            int progresso = bib.getProgressoAtual();

            // progresso maior que o novo total — ajusta
            if (total > 0 && progresso > total) {
                bib.setProgressoAtual(total);
            }

            // recalcula status
            if (total > 0 && bib.getProgressoAtual() >= total && !Boolean.TRUE.equals(bib.getEmLancamento())) {
                bib.setStatus(StatusBiblioteca.COMPLETO);
            } else if (bib.getStatus() == StatusBiblioteca.COMPLETO) {
                // estava completo mas o total aumentou — volta para acompanhando
                bib.setStatus(StatusBiblioteca.ACOMPANHANDO);
                bib.setEmLancamento(false);
            }
        }
        bibliotecaRepository.saveAll(bibliotecas);
    }

    public void deletar(Integer id) {
        Obra obra = buscarObraDoUsuario(id);
        obraRepository.delete(obra);
    }

    // --- Helpers ---

    private Obra toEntity(ObraRequest request) {
        Obra obra = new Obra();
        obra.setTitulo(request.getTitulo());
        obra.setDescricao(request.getDescricao());
        obra.setTipo(request.getTipo());
        obra.setImagemUrl(request.getImagemUrl());
        obra.setAutor(request.getAutor());
        obra.setEstudio(request.getEstudio());
        obra.setTotalUnidade(request.getTotalUnidade());
        obra.setGeneros(request.getGeneros() != null
                ? new ArrayList<>(request.getGeneros()) : new ArrayList<>());
        return obra;
    }

    private ObraResponse toResponse(Obra obra) {
        ObraResponse response = new ObraResponse();
        response.setIdObra(obra.getIdObra());
        response.setTitulo(obra.getTitulo());
        response.setDescricao(obra.getDescricao());
        response.setTipo(obra.getTipo());
        response.setImagemUrl(obra.getImagemUrl());
        response.setAutor(obra.getAutor());
        response.setEstudio(obra.getEstudio());
        response.setTotalUnidade(obra.getTotalUnidade());
        response.setGeneros(obra.getGeneros() == null ? List.of() : obra.getGeneros());
        return response;
    }

    public String salvarImagem(MultipartFile arquivo) {
        try {
            Path pasta = Paths.get(uploadDir);
            if (!Files.exists(pasta)) Files.createDirectories(pasta);
            String nomeArquivo = UUID.randomUUID() + "_" + arquivo.getOriginalFilename();
            Path destino = pasta.resolve(nomeArquivo);
            Files.copy(arquivo.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);
            return "/" + uploadDir + "/" + nomeArquivo;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar imagem");
        }
    }

    public ObraResponse uploadImagem(Integer id, MultipartFile arquivo) {
        Obra obra = obraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Obra não encontrada"));
        obra.setImagemUrl(salvarImagem(arquivo));
        return toResponse(obraRepository.save(obra));
    }
    
    private Obra buscarObraDoUsuario(Integer id) {
        Usuario usuario = authUtil.getUsuarioAutenticado();
        Obra obra = obraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Obra não encontrada"));
        if (!obra.getUsuario().getIdUsuario().equals(usuario.getIdUsuario())) {
            throw new RuntimeException("Acesso negado");
        }
        return obra;
    }
}