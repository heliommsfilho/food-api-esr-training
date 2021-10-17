package com.github.heliommsfilho.foodapi.assembler;

import com.github.heliommsfilho.foodapi.domain.model.Cozinha;
import com.github.heliommsfilho.foodapi.model.CozinhaModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CozinhaModelAssembler {
    
    @Autowired
    private ModelMapper modelMapper;
    
    public CozinhaModel toModel(Cozinha cozinha) {
        return modelMapper.map(cozinha, CozinhaModel.class);
    }
    
    public List<CozinhaModel> toCollectionModel(List<Cozinha> cozinhas) {
        return cozinhas.stream()
                .map(cozinha -> toModel(cozinha))
                .collect(Collectors.toList());
    }
}
