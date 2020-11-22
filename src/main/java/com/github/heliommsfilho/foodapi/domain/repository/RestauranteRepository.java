package com.github.heliommsfilho.foodapi.domain.repository;

import com.github.heliommsfilho.foodapi.domain.model.Restaurante;

import java.util.List;

public interface RestauranteRepository {

    List<Restaurante> todas();
    Restaurante buscar(Long id);
    Restaurante salvar(Restaurante restaurante);
    void remover(Restaurante restaurante);
}
