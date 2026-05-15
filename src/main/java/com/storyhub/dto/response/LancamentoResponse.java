package com.storyhub.dto.response;

import lombok.Data;

import java.time.LocalTime;

@Data
public class LancamentoResponse {
    private Integer idLancamento;
    private Integer obraId;
    private String tituloObra;
    private String frequencia;
    private Integer diaSemana;
    private Integer diaMes;
    private LocalTime horarioLancamento;
    private String imagemUrl;
    private String tipoObra;
}