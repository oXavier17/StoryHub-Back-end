package com.storyhub.repository;

import com.storyhub.entity.Lancamento;
import com.storyhub.enums.StatusBiblioteca;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface LancamentoRepository extends JpaRepository<Lancamento, Integer> {
    Optional<Lancamento> findByObra_IdObra(Integer obraId);
    boolean existsByObra_IdObra(Integer obraId);
    @Query("""
        select l from Lancamento l
        where l.obra.idObra in (
            select b.obra.idObra from Biblioteca b
            where b.usuario.idUsuario = :usuarioId
            and b.status = :status
        )
    """)
    List<Lancamento> findByUsuarioAcompanhando(
        @Param("usuarioId") Integer usuarioId,
        @Param("status") StatusBiblioteca status
    );
}