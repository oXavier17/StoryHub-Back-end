package com.storyhub.dto.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class VolumeResponse {
    private Integer idVolume;
    private Integer obraId;
    private String tituloObra;
    private Integer numeroVolume;
    private String isbn;
    private LocalDate dataLancamento;
}