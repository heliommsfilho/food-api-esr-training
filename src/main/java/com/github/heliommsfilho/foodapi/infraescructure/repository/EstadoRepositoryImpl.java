package com.github.heliommsfilho.foodapi.infraescructure.repository;

import com.github.heliommsfilho.foodapi.domain.model.Estado;
import com.github.heliommsfilho.foodapi.domain.repository.EstadoRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Component
public class EstadoRepositoryImpl implements EstadoRepository {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public List<Estado> listar() {
        return manager.createQuery("select e from Estado e", Estado.class)
                .getResultList();
    }

    @Override
    public Optional<Estado> buscar(Long id) {
        return Optional.ofNullable(manager.find(Estado.class, id));
    }

    @Transactional
    @Override
    public Estado salvar(Estado estado) {
        return manager.merge(estado);
    }

    @Transactional
    @Override
    public void remover(Long id) {
        Optional<Estado> estadoOptional = buscar(id);
        Estado estado = estadoOptional.orElseThrow(() -> new EmptyResultDataAccessException(1));
        manager.remove(estado);
    }

}
