package com.github.heliommsfilho.foodapi.domain.repository;

import com.github.heliommsfilho.foodapi.domain.model.Usuario;
import com.github.heliommsfilho.foodapi.infrastructure.repository.CustomJPARepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends CustomJPARepository<Usuario, Long> {
    
    Optional<Usuario> findByEmail(final String email);
}
