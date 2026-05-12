package com.storyhub.repository;

import com.storyhub.entity.ColecaoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ColecaoUsuarioRepository extends JpaRepository<ColecaoUsuario, Integer> {
    List<ColecaoUsuario> findByUsuario_IdUsuario(Integer usuarioId);
    Optional<ColecaoUsuario> findByUsuario_IdUsuarioAndVolume_IdVolume(Integer usuarioId, Integer volumeId);
    boolean existsByUsuario_IdUsuarioAndVolume_IdVolume(Integer usuarioId, Integer volumeId);
}