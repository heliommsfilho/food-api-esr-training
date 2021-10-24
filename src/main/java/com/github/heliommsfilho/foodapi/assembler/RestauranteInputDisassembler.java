package com.github.heliommsfilho.foodapi.assembler;

import com.github.heliommsfilho.foodapi.domain.model.Cidade;
import com.github.heliommsfilho.foodapi.domain.model.Cozinha;
import com.github.heliommsfilho.foodapi.domain.model.Restaurante;
import com.github.heliommsfilho.foodapi.model.input.RestauranteInput;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class RestauranteInputDisassembler {
    
    @Autowired
    private ModelMapper modelMapper;
    
    public Restaurante toDomainObject(RestauranteInput restauranteInput) {
        return modelMapper.map(restauranteInput, Restaurante.class);
    }
    
    public void copyToDomainObject(RestauranteInput restauranteInput, Restaurante restaurante) {
        // Para evitar org.hibernate.HibernateException: identifier of an instance of
        // com.algaworks.algafood.domain.model.Cozinha was altered from 1 to 2
        restaurante.setCozinha(new Cozinha());
        
        if (Objects.nonNull(restaurante.getEndereco())) {
            restaurante.getEndereco().setCidade(new Cidade());
        }
        
        modelMapper.map(restauranteInput, restaurante);
    }
}
