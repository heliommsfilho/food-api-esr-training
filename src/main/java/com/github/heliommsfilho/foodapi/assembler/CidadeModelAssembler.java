package com.github.heliommsfilho.foodapi.assembler;

import com.github.heliommsfilho.foodapi.domain.model.Cidade;
import com.github.heliommsfilho.foodapi.model.CidadeModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CidadeModelAssembler {
    
    @Autowired
    private ModelMapper modelMapper;
    
    public CidadeModel toModel(Cidade cidade) {
        return modelMapper.map(cidade, CidadeModel.class);
    }
    
    public List<CidadeModel> toCollectionModel(List<Cidade> cidades) {
        return cidades.stream()
                .map(cidade -> toModel(cidade))
                .collect(Collectors.toList());
    }
}
