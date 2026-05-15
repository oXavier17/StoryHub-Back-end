package com.storyhub.dto.response;

import com.storyhub.enums.Genero;
import com.storyhub.enums.TipoObra;
import lombok.Data;

import java.util.List;

@Data
public class ObraResponse {
    private Integer idObra;
    private String titulo;
    private String descricao;
    private Integer totalUnidade;
    private TipoObra tipo;
    private String imagemUrl;
    private String autor;
    private String estudio;
    private List<Genero> generos;
}