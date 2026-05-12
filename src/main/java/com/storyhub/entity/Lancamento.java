package com.storyhub.entity;

import java.time.LocalTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Lancamento")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lancamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idLancamento")
    private Integer idLancamento;

    @OneToOne
    @JoinColumn(name = "obraId", nullable = false)
    private Obra obra;

    @Column(nullable = false, length = 20)
    private String frequencia;

    private Integer diaSemana;

    private Integer diaMes;

    private LocalTime horarioLancamento;
}