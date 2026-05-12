package com.storyhub.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ColecaoUsuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColecaoUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idColecao")
    private Integer idColecao;

    @ManyToOne
    @JoinColumn(name = "usuarioId", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "volumeId", nullable = false)
    private Volume volume;

    @Column(nullable = false)
    private Boolean possui;

    @Column(nullable = false)
    private Boolean lido;

    private LocalDate dataCompra;

    private BigDecimal valorPago;
}