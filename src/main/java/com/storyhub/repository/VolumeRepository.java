package com.storyhub.repository;

import com.storyhub.entity.Volume;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VolumeRepository extends JpaRepository<Volume, Integer> {
    List<Volume> findByObra_IdObraOrderByNumeroVolumeAsc(Integer obraId);
    boolean existsByObra_IdObraAndNumeroVolume(Integer obraId, Integer numeroVolume);
}