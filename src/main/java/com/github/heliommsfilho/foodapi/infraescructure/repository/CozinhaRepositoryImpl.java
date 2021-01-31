package com.github.heliommsfilho.foodapi.infraescructure.repository;

import com.github.heliommsfilho.foodapi.domain.model.Cozinha;
import com.github.heliommsfilho.foodapi.domain.repository.CozinhaRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Component
public class CozinhaRepositoryImpl implements CozinhaRepository {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public List<Cozinha> listar() {
        return manager.createQuery("select c from Cozinha c", Cozinha.class).getResultList();
    }

    @Override
    public Optional<Cozinha> buscar(Long id) {
        return Optional.ofNullable(manager.find(Cozinha.class, id));
    }

    @Override
    public List<Cozinha> consultarPorNome(String nome) {
        return manager.createQuery("select c from Cozinha c where c.nome = :nome", Cozinha.class)
                      .setParameter("nome", nome).getResultList();
    }

    @Transactional
    @Override
    public Cozinha salvar(Cozinha cozinha) {
        return manager.merge(cozinha);
    }

    @Transactional
    @Override
    public void remover(Long id) {
        Optional<Cozinha> cozinhaOptional = buscar(id);
        Cozinha cozinha = cozinhaOptional.orElseThrow(() -> new EmptyResultDataAccessException(1));
        manager.remove(cozinha);
    }
}
