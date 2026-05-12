package com.storyhub.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ColecaoUsuarioRequest {

    @NotNull
    private Integer volumeId;

    @NotNull
    private Boolean possui;

    @NotNull
    private Boolean lido;

    private LocalDate dataCompra;

    private BigDecimal valorPago;
}