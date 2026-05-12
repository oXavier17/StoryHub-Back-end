package com.storyhub.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HistoricoResponse {
    private Integer idHistorico;
    private Integer bibliotecaId;
    private String tituloObra;
    private Integer capitulo;
    private LocalDateTime dataRegistro;
}