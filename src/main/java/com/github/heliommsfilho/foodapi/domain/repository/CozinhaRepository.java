package com.github.heliommsfilho.foodapi.domain.repository;

import com.github.heliommsfilho.foodapi.domain.model.Cozinha;

import java.util.List;
import java.util.Optional;

public interface CozinhaRepository {

    List<Cozinha> listar();
    Optional<Cozinha> buscar(Long id);
    Cozinha salvar(Cozinha cozinha);
    void remover(Long id);
}
