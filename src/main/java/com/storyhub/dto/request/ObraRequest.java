package com.storyhub.dto.request;

import com.storyhub.enums.TipoObra;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class ObraRequest {

    @NotBlank
    @Size(max = 100)
    private String titulo;

    @NotBlank
    private String descricao;

    @NotNull
    private TipoObra tipo;

    @Size(max = 500)
    private String imagemUrl;

    @Size(max = 100)
    private String autor;

    @Size(max = 100)
    private String estudio;

    private List<Integer> generoIds;
}