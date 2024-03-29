package com.github.heliommsfilho.foodapi.core.modelmapper;

import com.github.heliommsfilho.foodapi.domain.model.Endereco;
import com.github.heliommsfilho.foodapi.domain.model.ItemPedido;
import com.github.heliommsfilho.foodapi.model.EnderecoModel;
import com.github.heliommsfilho.foodapi.model.input.ItemPedidoInput;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    
    @Bean
    public ModelMapper modelMapper() {
        final ModelMapper modelMapper = new ModelMapper();
        
        final TypeMap<Endereco, EnderecoModel> enderecoTypeMap = modelMapper.createTypeMap(Endereco.class, EnderecoModel.class);
        enderecoTypeMap.<String>addMapping(src -> src.getCidade().getEstado().getNome(),
                                          ((dest, value) -> dest.getCidade().setEstado(value)));
    
        modelMapper.createTypeMap(ItemPedidoInput.class, ItemPedido.class)
                .addMappings(mapper -> mapper.skip(ItemPedido::setId));
    
        return modelMapper;
    }
}
