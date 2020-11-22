package com.github.heliommsfilho.foodapi.infraescructure.repository;

import com.github.heliommsfilho.foodapi.domain.model.Restaurante;
import com.github.heliommsfilho.foodapi.domain.repository.RestauranteRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Component
public class RestauranteRepositoryImpl implements RestauranteRepository {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public List<Restaurante> todos() {
        return manager.createQuery("from Restaurante", Restaurante.class)
                .getResultList();
    }

    @Override
    public Optional<Restaurante> buscar(Long id) {
        return Optional.ofNullable(manager.find(Restaurante.class, id));
    }

    @Transactional
    @Override
    public Restaurante salvar(Restaurante restaurante) {
        return manager.merge(restaurante);
    }

    @Transactional
    @Override
    public void remover(Restaurante restaurante) {
        Optional<Restaurante> restauranteOptional = buscar(restaurante.getId());
        restauranteOptional.ifPresent(r ->manager.remove(r));
    }

}
