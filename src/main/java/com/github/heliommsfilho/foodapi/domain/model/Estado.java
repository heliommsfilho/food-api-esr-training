package com.github.heliommsfilho.foodapi.domain.model;

import com.github.heliommsfilho.foodapi.core.validation.Groups;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "estado")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Estado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = Groups.EstadoId.class)
    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank
    @Column(name = "nome")
    private String nome;
}
