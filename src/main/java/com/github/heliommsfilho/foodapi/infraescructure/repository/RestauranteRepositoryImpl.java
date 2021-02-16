package com.github.heliommsfilho.foodapi.infraescructure.repository;

import com.github.heliommsfilho.foodapi.domain.model.Restaurante;
import com.github.heliommsfilho.foodapi.domain.repository.RestauranteRepositoryQueries;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Repository
public class RestauranteRepositoryImpl implements RestauranteRepositoryQueries {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Restaurante> find(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal) {
        var jpql = new StringBuilder();
        jpql.append("FROM Restaurante WHERE 0 = 0 ");

        var parametros = new HashMap<String, Object>();


        if (!StringUtils.isEmpty(nome)) {
            jpql.append("AND nome = :nome ");
            parametros.put("nome", "%" + nome + "%");
        }

        if (Objects.nonNull(taxaFreteInicial)) {
            jpql.append("AND taxaFrete >= :taxaFreteInicial ");
            parametros.put("taxaInicial", taxaFreteInicial);
        }

        if (Objects.nonNull(taxaFreteFinal)) {
            jpql.append("AND taxaFrete <= :taxaFreteFinal ");
            parametros.put("taxaFinal", taxaFreteFinal);
        }

        TypedQuery<Restaurante> query = entityManager.createQuery(jpql.toString(), Restaurante.class);
        parametros.forEach(query::setParameter);

        return query.getResultList();
    }
}
