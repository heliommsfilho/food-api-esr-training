package com.github.heliommsfilho.foodapi.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface CustomJPARepository<T, ID> extends JpaRepository<T, ID> {

    Optional<T> buscarPrimeiro();
    
    void detach(T entity);
}
