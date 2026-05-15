package com.storyhub.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ColecaoUsuarioResponse {
    private Integer idColecao;
    private Integer volumeId;
    private String tituloObra;
    private Integer numeroVolume;
    private String imagemUrl;
    private Boolean possui;
    private Boolean lido;
    private LocalDate dataCompra;
    private BigDecimal valorPago;
}