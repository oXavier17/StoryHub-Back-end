package com.storyhub.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.storyhub.enums.StatusBiblioteca;

import java.math.BigDecimal;
import java.util.List;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Biblioteca")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Biblioteca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idBiblioteca")
    private Integer idBiblioteca;

    @ManyToOne
    @JoinColumn(name = "usuarioId", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "obraId", nullable = false)
    private Obra obra;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private StatusBiblioteca status;

    @Column(nullable = false)
    private Boolean emLancamento;

    @Column(nullable = false)
    private Integer progressoAtual;

    @Column(nullable = false)
    private Boolean favorito;

    private BigDecimal nota;

    @OneToMany(mappedBy = "biblioteca", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Historico> historico;
}