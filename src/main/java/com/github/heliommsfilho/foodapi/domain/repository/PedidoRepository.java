package com.github.heliommsfilho.foodapi.domain.repository;

import com.github.heliommsfilho.foodapi.domain.model.Pedido;
import com.github.heliommsfilho.foodapi.infrastructure.repository.CustomJPARepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends CustomJPARepository<Pedido, Long> {
    
    @Query("""
        FROM Pedido p JOIN FETCH p.cliente
                      JOIN FETCH p.restaurante r
                      JOIN FETCH r.cozinha
    """)
    List<Pedido> findAll();
}
