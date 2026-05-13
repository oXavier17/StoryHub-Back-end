package com.storyhub.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DashboardResponse {
    private Long totalObras;
    private Long totalLendo;
    private Long totalCompletos;
    private Long totalFavoritos;
    private List<BibliotecaResponse> ultimasAdicionadas;
}