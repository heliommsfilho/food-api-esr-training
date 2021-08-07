package com.github.heliommsfilho.foodapi.domain.model;

import com.github.heliommsfilho.foodapi.core.validation.Groups;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.groups.ConvertGroup;

@Entity
@Table(name = "cidade")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Cidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank
    @Column(name = "nome")
    private String nome;

    @Valid
    @ConvertGroup(to = Groups.EstadoId.class)
    @ManyToOne
    @JoinColumn(name = "estado_id")
    private Estado estado;
}
