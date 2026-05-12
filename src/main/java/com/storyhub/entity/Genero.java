package com.storyhub.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Genero")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Genero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idGenero")
    private Integer idGenero;

    @Column(nullable = false, length = 50)
    private String nome;

    @ManyToMany(mappedBy = "generos")
    @JsonIgnore
    private List<Obra> obras;
}