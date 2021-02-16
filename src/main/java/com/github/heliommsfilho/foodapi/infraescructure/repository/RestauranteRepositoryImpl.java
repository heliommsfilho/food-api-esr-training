package com.github.heliommsfilho.foodapi.infraescructure.repository;

import com.github.heliommsfilho.foodapi.domain.model.Restaurante;
import com.github.heliommsfilho.foodapi.domain.repository.RestauranteRepository;
import com.github.heliommsfilho.foodapi.domain.repository.RestauranteRepositoryQueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.github.heliommsfilho.foodapi.infraescructure.repository.RestauranteSpecs.comFreteGratis;
import static com.github.heliommsfilho.foodapi.infraescructure.repository.RestauranteSpecs.comNomeSemelhante;

@Repository
public class RestauranteRepositoryImpl implements RestauranteRepositoryQueries {

    @PersistenceContext
    private EntityManager entityManager;

    private final RestauranteRepository restauranteRepository;

    @Autowired @Lazy
    public RestauranteRepositoryImpl(RestauranteRepository restauranteRepository) {
        this.restauranteRepository = restauranteRepository;
    }

    @Override
    public List<Restaurante> find(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal) {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var criteriaQuery = criteriaBuilder.createQuery(Restaurante.class);
        var root = criteriaQuery.from(Restaurante.class);

        criteriaQuery.where(criarRestricoes(criteriaBuilder, root, nome, taxaFreteInicial, taxaFreteFinal));

        var typedQuery = entityManager.createQuery(criteriaQuery);

        return typedQuery.getResultList();
    }

    @Override
    public List<Restaurante> findComFreteGratis(String nome) {
        return restauranteRepository.findAll(comFreteGratis().and(comNomeSemelhante(nome)));
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
