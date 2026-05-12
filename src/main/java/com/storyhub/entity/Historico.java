package com.storyhub.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Historico")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Historico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idHistorico")
    private Integer idHistorico;

    @ManyToOne
    @JoinColumn(name = "bibliotecaId", nullable = false)
    private Biblioteca biblioteca;

    @Column(nullable = false)
    private Integer capitulo;

    @Column(nullable = false)
    private LocalDateTime dataRegistro;
}