package com.storyhub.repository;

import com.storyhub.entity.Genero;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GeneroRepository extends JpaRepository<Genero, Integer> {
    boolean existsByNomeIgnoreCase(String nome);
}