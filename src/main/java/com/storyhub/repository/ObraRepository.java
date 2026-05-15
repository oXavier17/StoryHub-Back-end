package com.storyhub.repository;

import com.storyhub.entity.Obra;
import com.storyhub.enums.TipoObra;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ObraRepository extends JpaRepository<Obra, Integer> {
    List<Obra> findByUsuario_IdUsuario(Integer usuarioId);
    boolean existsByTituloIgnoreCaseAndTipoAndUsuario_IdUsuario(String titulo, TipoObra tipo, Integer usuarioId);
}