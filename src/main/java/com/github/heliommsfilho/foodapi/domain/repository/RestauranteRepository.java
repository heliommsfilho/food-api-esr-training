package com.github.heliommsfilho.foodapi.domain.repository;

import com.github.heliommsfilho.foodapi.domain.model.Restaurante;

import java.util.List;
import java.util.Optional;

public interface RestauranteRepository {

    List<Restaurante> todos();
    Optional<Restaurante> buscar(Long id);
    Restaurante salvar(Restaurante restaurante);
    void remover(Restaurante restaurante);
}
