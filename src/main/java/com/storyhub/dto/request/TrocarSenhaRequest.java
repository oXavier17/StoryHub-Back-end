package com.storyhub.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TrocarSenhaRequest {

    @NotBlank
    private String senhaAtual;

    @NotBlank
    @Size(min = 6, max = 100)
    private String novaSenha;
}