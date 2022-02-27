package com.github.heliommsfilho.foodapi.infrastructure.service;

import com.github.heliommsfilho.foodapi.domain.filter.VendaDiariaFilter;
import com.github.heliommsfilho.foodapi.domain.model.Pedido;
import com.github.heliommsfilho.foodapi.domain.model.dto.VendaDiaria;
import com.github.heliommsfilho.foodapi.domain.service.VendaQueryService;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

@Repository
public class VendaQueryServiceImpl implements VendaQueryService {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    public List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter filtro) {
        var builder = entityManager.getCriteriaBuilder();
        var query = builder.createQuery(VendaDiaria.class);
        var root = query.from(Pedido.class);
        
        var functionDateDataCricao = builder.function("date", Date.class, root.get("dataCriacao"));
        var selection = builder.construct(VendaDiaria.class,
                functionDateDataCricao,
                builder.count(root.get("id")),
                builder.sum(root.get("valorTotal")));
        
        query.select(selection);
        query.groupBy(functionDateDataCricao);
        
        return entityManager.createQuery(query).getResultList();
    }
}
