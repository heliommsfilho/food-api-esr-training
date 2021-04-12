package com.github.heliommsfilho.foodapi.domain.repository;

import com.github.heliommsfilho.foodapi.domain.model.Restaurante;
import com.github.heliommsfilho.foodapi.infraestructure.repository.CustomJPARepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface RestauranteRepository extends CustomJPARepository<Restaurante, Long>, RestauranteRepositoryQueries,
                                               JpaSpecificationExecutor<Restaurante> {

    @Query("from Restaurante r join r.cozinha")
    List<Restaurante> findAll();
    List<Restaurante> findByTaxaFreteBetween(BigDecimal taxaInicial, BigDecimal taxaFinal);
    List<Restaurante> findByNomeContainingAndCozinhaId(String nome, Long cozinha);
}
