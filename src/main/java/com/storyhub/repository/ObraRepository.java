package com.storyhub.repository;

import com.storyhub.entity.Obra;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ObraRepository extends JpaRepository<Obra, Integer> {
    Optional<Obra> findByTituloIgnoreCase(String titulo);
    boolean existsByTituloIgnoreCase(String titulo);
}