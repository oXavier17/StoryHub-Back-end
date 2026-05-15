package com.storyhub.dto.response;

import java.math.BigDecimal;
import java.util.List;

import com.storyhub.enums.Genero;
import com.storyhub.enums.StatusBiblioteca;
import lombok.Data;

@Data
public class BibliotecaResponse {
    private Integer idBiblioteca;
    private Integer obraId;
    private String tituloObra;
    private String tipoObra;
    private String imagemUrl;
    private List<Genero> generos;
    private StatusBiblioteca status;
    private Integer progressoAtual;
    private Integer totalUnidade; 
    private Boolean emLancamento;
    private Boolean favorito;
    private BigDecimal nota;
}