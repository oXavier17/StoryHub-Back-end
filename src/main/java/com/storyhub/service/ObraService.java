package com.storyhub.service;

import com.storyhub.dto.request.ObraRequest;
import com.storyhub.dto.response.ObraResponse;
import com.storyhub.entity.Obra;
import com.storyhub.exception.ResourceNotFoundException;
import com.storyhub.repository.ObraRepository;
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

    public ObraResponse criar(ObraRequest request, MultipartFile imagem) {
        if (obraRepository.existsByTituloIgnoreCase(request.getTitulo())) {
            throw new RuntimeException("Já existe uma obra com esse título");
        }

        Obra obra = toEntity(request);
        obra = obraRepository.save(obra);

        if (imagem != null && !imagem.isEmpty()) {
            try {
                String caminho = salvarImagem(imagem);
                obra.setImagemUrl(caminho);
                obra = obraRepository.save(obra);
            } catch (Exception e) {
                obraRepository.delete(obra); // rollback manual
                throw new RuntimeException("Erro ao salvar imagem: " + e.getMessage());
            }
        }

        return toResponse(obra);
    }

    public ObraResponse atualizar(Integer id, ObraRequest request) {
        Obra obra = obraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Obra não encontrada"));

        obra.setTitulo(request.getTitulo());
        obra.setDescricao(request.getDescricao());
        obra.setTipo(request.getTipo());
        obra.setImagemUrl(request.getImagemUrl());
        obra.setAutor(request.getAutor());
        obra.setEstudio(request.getEstudio());
        obra.setGeneros(request.getGeneros() != null ? new ArrayList<>(request.getGeneros()) : new ArrayList<>());

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
        obra.setImagemUrl(request.getImagemUrl());
        obra.setAutor(request.getAutor());
        obra.setEstudio(request.getEstudio());
        obra.setGeneros(request.getGeneros() != null ? request.getGeneros() : new ArrayList<>());
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
}