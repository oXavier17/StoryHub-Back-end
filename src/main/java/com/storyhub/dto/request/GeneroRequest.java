package com.storyhub.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class GeneroRequest {

    @NotBlank
    @Size(max = 50)
    private String nome;
}