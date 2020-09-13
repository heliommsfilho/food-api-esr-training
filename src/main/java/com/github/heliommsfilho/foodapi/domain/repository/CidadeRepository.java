package com.github.heliommsfilho.foodapi.domain.repository;

import com.github.heliommsfilho.foodapi.domain.model.Cidade;

import java.util.List;

public interface CidadeRepository {

    List<Cidade> listar();
    Cidade buscar(Long id);
    Cidade salvar(Cidade cidade);
    void remover(Cidade cidade);
}
