package com.github.heliommsfilho.foodapi.domain.repository;

import com.github.heliommsfilho.foodapi.domain.model.Usuario;
import com.github.heliommsfilho.foodapi.infrastructure.repository.CustomJPARepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends CustomJPARepository<Usuario, Long> {

}
