package com.github.heliommsfilho.foodapi.infraescructure.repository;

import com.github.heliommsfilho.foodapi.domain.model.Restaurante;
import com.github.heliommsfilho.foodapi.domain.repository.RestauranteRepositoryQueries;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class RestauranteRepositoryImpl implements RestauranteRepositoryQueries {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Restaurante> find(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal) {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var criteriaQuery = criteriaBuilder.createQuery(Restaurante.class);
        var root = criteriaQuery.from(Restaurante.class);

        criteriaQuery.where(criarRestricoes(criteriaBuilder, root, nome, taxaFreteInicial, taxaFreteFinal));

        var typedQuery = entityManager.createQuery(criteriaQuery);

        return typedQuery.getResultList();
    }

    private Predicate[] criarRestricoes(CriteriaBuilder builder, Root<Restaurante> root, String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal) {
        var restricoes = new ArrayList<Predicate>();

        if (!StringUtils.isEmpty(nome)) {
            restricoes.add(builder.like(root.get("nome"), "%" + nome + "%"));
        }

        if (Objects.nonNull(taxaFreteInicial)) {
            restricoes.add(builder.greaterThanOrEqualTo(root.get("taxaFrete"), taxaFreteInicial));
        }

        if (Objects.nonNull(taxaFreteFinal)) {
            restricoes.add(builder.lessThanOrEqualTo(root.get("taxaFrete"), taxaFreteFinal));
        }

        return restricoes.toArray(new Predicate[0]);
    }
}
