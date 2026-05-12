package com.storyhub.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class VolumeRequest {

    @NotNull
    private Integer obraId;

    @NotNull
    @Min(1)
    private Integer numeroVolume;

    private String isbn;

    private LocalDate dataLancamento;
}