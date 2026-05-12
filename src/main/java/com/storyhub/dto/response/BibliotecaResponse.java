package com.storyhub.dto.response;

import com.storyhub.enums.StatusBiblioteca;
import lombok.Data;

@Data
public class BibliotecaResponse {
    private Integer idBiblioteca;
    private Integer obraId;
    private String tituloObra;
    private String tipoObra;
    private StatusBiblioteca status;
    private Integer progressoAtual;
    private Integer totalUnidade;
    private Boolean favorito;
}