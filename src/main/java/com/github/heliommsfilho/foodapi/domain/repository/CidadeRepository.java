package com.github.heliommsfilho.foodapi.domain.repository;

import com.github.heliommsfilho.foodapi.domain.model.Cidade;

import java.util.List;
import java.util.Optional;

public interface CidadeRepository {

    List<Cidade> listar();
    Optional<Cidade> buscar(Long id);
    Cidade salvar(Cidade cidade);
    void remover(Long id);
}
