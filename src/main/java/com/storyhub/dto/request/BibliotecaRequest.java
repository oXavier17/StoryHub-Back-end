package com.storyhub.dto.request;

import com.storyhub.enums.StatusBiblioteca;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BibliotecaRequest {

    @NotNull
    private Integer obraId;

    @NotNull
    private StatusBiblioteca status;

    @NotNull
    @Min(0)
    private Integer progressoAtual;

    @NotNull
    @Min(0)
    private Integer totalUnidade;

    @NotNull
    private Boolean favorito;
}