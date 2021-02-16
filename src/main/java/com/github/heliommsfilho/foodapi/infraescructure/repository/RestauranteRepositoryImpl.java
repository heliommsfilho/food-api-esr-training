package com.github.heliommsfilho.foodapi.infraescructure.repository;

import com.github.heliommsfilho.foodapi.domain.model.Restaurante;
import com.github.heliommsfilho.foodapi.domain.repository.RestauranteRepositoryQueries;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.List;

@Repository
public class RestauranteRepositoryImpl implements RestauranteRepositoryQueries {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Restaurante> find(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal) {
        String jpql = "from Restaurante where nome like :nome and taxaFrete between :taxaInicial and :taxaFinal";

        return entityManager.createQuery(jpql, Restaurante.class)
                            .setParameter("nome", "%" + nome + "%")
                            .setParameter("taxaInicial", taxaFreteInicial)
                            .setParameter("taxaFinal", taxaFreteFinal)
                            .getResultList();
    }
}
