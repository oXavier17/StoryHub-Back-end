package com.storyhub.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AtualizarPerfilRequest {

    @NotBlank
    @Size(max = 200)
    private String nome;

    @NotBlank
    @Email
    @Size(max = 100)
    private String email;
}