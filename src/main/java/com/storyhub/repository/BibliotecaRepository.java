package com.storyhub.repository;

import com.storyhub.entity.Biblioteca;
import com.storyhub.enums.StatusBiblioteca;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BibliotecaRepository extends JpaRepository<Biblioteca, Integer> {
    List<Biblioteca> findByUsuario_IdUsuario(Integer usuarioId);
    Optional<Biblioteca> findByUsuario_IdUsuarioAndObra_IdObra(Integer usuarioId, Integer obraId);
    boolean existsByUsuario_IdUsuarioAndObra_IdObra(Integer usuarioId, Integer obraId);
    long countByUsuario_IdUsuario(Integer usuarioId);
    long countByUsuario_IdUsuarioAndStatus(Integer usuarioId, StatusBiblioteca status);
    long countByUsuario_IdUsuarioAndFavoritoTrue(Integer usuarioId);
    List<Biblioteca> findTop5ByUsuario_IdUsuarioOrderByIdBibliotecaDesc(Integer usuarioId);
    List<Biblioteca> findByObra_IdObra(Integer obraId);
}