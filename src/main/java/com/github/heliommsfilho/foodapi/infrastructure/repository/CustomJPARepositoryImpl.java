package com.github.heliommsfilho.foodapi.infrastructure.repository;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import java.util.Optional;

public class CustomJPARepositoryImpl<T, ID> extends SimpleJpaRepository<T, ID> implements CustomJPARepository<T, ID> {

    private EntityManager entityManager;

    public CustomJPARepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);

        this.entityManager = entityManager;
    }

    @Override
    public Optional<T> buscarPrimeiro() {
        var jpql = String.format("FROM %s", getDomainClass().getName());

        T entity = entityManager.createQuery(jpql, getDomainClass()).setMaxResults(1).getSingleResult();
        return Optional.ofNullable(entity);
    }
    
    @Override
    public void detach(T entity) {
        entityManager.detach(entity);
    }
}
