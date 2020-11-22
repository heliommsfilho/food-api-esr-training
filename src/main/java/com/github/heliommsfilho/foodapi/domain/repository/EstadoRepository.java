package com.github.heliommsfilho.foodapi.domain.repository;

import com.github.heliommsfilho.foodapi.domain.model.Estado;

import java.util.List;
import java.util.Optional;

public interface EstadoRepository {

    List<Estado> listar();
    Optional<Estado> buscar(Long id);
    Estado salvar(Estado estado);
    void remover(Long id);
}
