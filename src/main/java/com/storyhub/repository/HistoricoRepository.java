package com.storyhub.repository;

import com.storyhub.entity.Historico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoricoRepository extends JpaRepository<Historico, Integer> {
    List<Historico> findByBiblioteca_IdBibliotecaOrderByDataRegistroDesc(Integer bibliotecaId);
}