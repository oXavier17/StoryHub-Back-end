package com.storyhub.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import java.util.List;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Volume")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Volume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idVolume")
    private Integer idVolume;

    @ManyToOne
    @JoinColumn(name = "obraId", nullable = false)
    private Obra obra;

    @Column(nullable = false)
    private Integer numeroVolume;

    @Column(length = 30)
    private String isbn;

    private LocalDate dataLancamento;

    @OneToMany(mappedBy = "volume", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ColecaoUsuario> colecoes;
}