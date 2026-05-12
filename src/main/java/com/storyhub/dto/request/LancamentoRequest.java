package com.storyhub.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalTime;

@Data
public class LancamentoRequest {

    @NotNull
    private Integer obraId;

    @NotBlank
    private String frequencia; // DIARIO, SEMANAL, MENSAL, IRREGULAR

    @Min(0) @Max(6)
    private Integer diaSemana;

    @Min(1) @Max(31)
    private Integer diaMes;

    private LocalTime horarioLancamento;
}