package com.storyhub.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.storyhub.enums.Genero;
import com.storyhub.enums.TipoObra;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Obra")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Obra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idObra")
    private Integer idObra;

    @Column(nullable = false, length = 100)
    private String titulo;

    @Column(nullable = false, columnDefinition = "varchar(max)")
    private String descricao;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private TipoObra tipo;

    @Column(length = 500)
    private String imagemUrl;

    @Column(length = 100)
    private String autor;

    @Column(length = 100)
    private String estudio;

    @ElementCollection
    @CollectionTable(
        name = "ObraGenero",
        joinColumns = @JoinColumn(name = "obraId")
    )
    @Column(name = "generoId")
    @Enumerated(EnumType.ORDINAL)
    private List<Genero> generos = new ArrayList<>();

    @OneToMany(mappedBy = "obra", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Biblioteca> bibliotecas;

    @OneToOne(mappedBy = "obra", cascade = CascadeType.ALL)
    @JsonIgnore
    private Lancamento lancamento;

    @OneToMany(mappedBy = "obra", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Volume> volumes;
}