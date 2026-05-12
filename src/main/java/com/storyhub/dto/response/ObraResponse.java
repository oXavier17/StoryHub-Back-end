package com.storyhub.dto.response;

import com.storyhub.enums.TipoObra;
import lombok.Data;

import java.util.List;

@Data
public class ObraResponse {
    private Integer idObra;
    private String titulo;
    private String descricao;
    private TipoObra tipo;
    private String autor;
    private String estudio;
    private List<String> generos;
}