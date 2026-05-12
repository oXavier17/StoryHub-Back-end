package com.storyhub.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class HistoricoRequest {

    @NotNull
    private Integer bibliotecaId;

    @NotNull
    @Min(1)
    private Integer capitulo;
}
