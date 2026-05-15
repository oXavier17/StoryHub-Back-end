package com.storyhub.dto.response;

import lombok.Data;

@Data
public class UsuarioResponse {
    private Integer idUsuario;
    private String nome;
    private String email;
    private String fotoPerfil;
}