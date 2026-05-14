package com.storyhub.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CriarVolumesLoteRequest {

    @NotNull
    private Integer obraId;

    @NotNull
    @Min(1)
    private Integer quantidade;
}