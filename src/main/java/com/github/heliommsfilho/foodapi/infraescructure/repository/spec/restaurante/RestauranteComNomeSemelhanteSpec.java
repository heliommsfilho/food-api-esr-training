package com.github.heliommsfilho.foodapi.infraescructure.repository.spec.restaurante;

import com.github.heliommsfilho.foodapi.domain.model.Restaurante;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;

@AllArgsConstructor
public class RestauranteComNomeSemelhanteSpec implements Specification<Restaurante> {

    private final String nome;

    @Override
    public Predicate toPredicate(Root<Restaurante> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        return builder.like(root.get("nome"), String.format("%%%s%%", nome));
    }
}
