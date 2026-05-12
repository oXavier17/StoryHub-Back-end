package com.storyhub.service;

import com.storyhub.dto.request.VolumeRequest;
import com.storyhub.dto.response.VolumeResponse;
import com.storyhub.entity.Obra;
import com.storyhub.entity.Volume;
import com.storyhub.exception.ResourceNotFoundException;
import com.storyhub.repository.ObraRepository;
import com.storyhub.repository.VolumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VolumeService {

    private final VolumeRepository volumeRepository;
    private final ObraRepository obraRepository;

    public List<VolumeResponse> listarPorObra(Integer obraId) {
        return volumeRepository.findByObra_IdObraOrderByNumeroVolumeAsc(obraId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public VolumeResponse buscarPorId(Integer id) {
        Volume volume = volumeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Volume não encontrado"));
        return toResponse(volume);
    }

    public VolumeResponse criar(VolumeRequest request) {
        if (volumeRepository.existsByObra_IdObraAndNumeroVolume(
                request.getObraId(), request.getNumeroVolume())) {
            throw new RuntimeException("Esse volume já existe para essa obra");
        }

        Obra obra = obraRepository.findById(request.getObraId())
                .orElseThrow(() -> new ResourceNotFoundException("Obra não encontrada"));

        Volume volume = new Volume();
        volume.setObra(obra);
        volume.setNumeroVolume(request.getNumeroVolume());
        volume.setIsbn(request.getIsbn());
        volume.setDataLancamento(request.getDataLancamento());

        return toResponse(volumeRepository.save(volume));
    }

    public VolumeResponse atualizar(Integer id, VolumeRequest request) {
        Volume volume = volumeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Volume não encontrado"));

        Obra obra = obraRepository.findById(request.getObraId())
                .orElseThrow(() -> new ResourceNotFoundException("Obra não encontrada"));

        volume.setObra(obra);
        volume.setNumeroVolume(request.getNumeroVolume());
        volume.setIsbn(request.getIsbn());
        volume.setDataLancamento(request.getDataLancamento());

        return toResponse(volumeRepository.save(volume));
    }

    public void deletar(Integer id) {
        if (!volumeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Volume não encontrado");
        }
        volumeRepository.deleteById(id);
    }

    // --- Helpers ---

    private VolumeResponse toResponse(Volume volume) {
        VolumeResponse response = new VolumeResponse();
        response.setIdVolume(volume.getIdVolume());
        response.setObraId(volume.getObra().getIdObra());
        response.setTituloObra(volume.getObra().getTitulo());
        response.setNumeroVolume(volume.getNumeroVolume());
        response.setIsbn(volume.getIsbn());
        response.setDataLancamento(volume.getDataLancamento());
        return response;
    }
}