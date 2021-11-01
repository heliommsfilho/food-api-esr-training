package com.github.heliommsfilho.foodapi.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String senha;

    @ManyToMany
    @JoinTable(name = "usuario_grupo",
               joinColumns = @JoinColumn(name = "usuario_id"),
               inverseJoinColumns = @JoinColumn(name = "grupo_id"))
    private Set<Grupo> grupos = new HashSet<>();
    
    public boolean senhaCoincideCom(String senha) {
        return getSenha().equals(senha);
    }
    
    public boolean senhaNaoCoincideCom(String senha) {
        return !senhaCoincideCom(senha);
    }
    
    public boolean removerGrupo(Grupo grupo) {
        return getGrupos().remove(grupo);
    }
    
    public boolean adicionarGrupo(Grupo grupo) {
        return getGrupos().add(grupo);
    }
}
