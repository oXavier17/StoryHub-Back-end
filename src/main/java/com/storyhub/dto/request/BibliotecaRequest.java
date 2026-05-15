package com.storyhub.dto.request;

import java.math.BigDecimal;

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
    private Boolean emLancamento;

    @NotNull
    @Min(0)
    private Integer progressoAtual;

    @NotNull
    private Boolean favorito;

    private BigDecimal nota;
}