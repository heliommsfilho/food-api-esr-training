package com.github.heliommsfilho.foodapi.assembler;

import com.github.heliommsfilho.foodapi.domain.model.Grupo;
import com.github.heliommsfilho.foodapi.model.input.GrupoInput;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GrupoInputDisassembler {
    
    @Autowired
    private ModelMapper modelMapper;
    
    public Grupo toDomainObject(GrupoInput grupoInput) {
        return modelMapper.map(grupoInput, Grupo.class);
    }
    
    public void copyToDomainObject(GrupoInput grupoInput, Grupo grupo) {
        modelMapper.map(grupoInput, grupo);
    }
}
