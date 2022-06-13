package com.github.heliommsfilho.foodapi.infrastructure.service.query;

import com.github.heliommsfilho.foodapi.domain.filter.VendaDiariaFilter;
import com.github.heliommsfilho.foodapi.domain.model.Pedido;
import com.github.heliommsfilho.foodapi.domain.model.PedidoStatus;
import com.github.heliommsfilho.foodapi.domain.model.dto.VendaDiaria;
import com.github.heliommsfilho.foodapi.domain.service.VendaQueryService;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class VendaQueryServiceImpl implements VendaQueryService {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    public List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter filtro, String timeOffset) {
        var builder = entityManager.getCriteriaBuilder();
        var query = builder.createQuery(VendaDiaria.class);
        var root = query.from(Pedido.class);
        
        aplicarFiltros(builder, query, root, filtro);
        
        var functionConvertTzDataCriacao = builder.function("convert_tz", Date.class, root.get("dataCriacao"), builder.literal("+00:00"),
                builder.literal(timeOffset));
        var functionDateDataCricao = builder.function("date", Date.class, functionConvertTzDataCriacao);
        var selection = builder.construct(VendaDiaria.class,
                functionDateDataCricao,
                builder.count(root.get("id")),
                builder.sum(root.get("valorTotal")));
        
        query.select(selection);
        query.groupBy(functionDateDataCricao);
        
        return entityManager.createQuery(query).getResultList();
    }
    
    private void aplicarFiltros(CriteriaBuilder builder, CriteriaQuery<VendaDiaria> query, Root<Pedido> root, VendaDiariaFilter filtro) {
        var predicates = new ArrayList<Predicate>(1);
        
        predicates.add(root.get("status").in(PedidoStatus.CONFIRMADO, PedidoStatus.ENTREGUE));
    
        if (filtro.getRestauranteId() != null) {
            predicates.add(builder.equal(root.get("restaurante"), filtro.getRestauranteId()));
        }
    
        if (filtro.getDataCriacaoInicio() != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("dataCriacao"), filtro.getDataCriacaoInicio()));
        }
    
        if (filtro.getDataCriacaoFim() != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("dataCriacao"), filtro.getDataCriacaoFim()));
        }
    
        query.where(predicates.toArray(new Predicate[0]));
    }
}
