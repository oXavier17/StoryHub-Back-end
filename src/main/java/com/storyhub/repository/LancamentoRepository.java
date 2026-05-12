package com.storyhub.repository;

import com.storyhub.entity.Lancamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LancamentoRepository extends JpaRepository<Lancamento, Integer> {
    Optional<Lancamento> findByObra_IdObra(Integer obraId);
    boolean existsByObra_IdObra(Integer obraId);
}