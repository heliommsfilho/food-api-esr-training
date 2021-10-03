package com.github.heliommsfilho.foodapi.core.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.heliommsfilho.foodapi.domain.model.Cidade;
import com.github.heliommsfilho.foodapi.domain.model.Cozinha;
import com.github.heliommsfilho.foodapi.domain.model.Restaurante;
import com.github.heliommsfilho.foodapi.model.mixin.CidadeMixin;
import com.github.heliommsfilho.foodapi.model.mixin.CozinhaMixin;
import com.github.heliommsfilho.foodapi.model.mixin.RestauranteMixin;
import org.springframework.stereotype.Component;

@Component
public class JacksonMixinModule extends SimpleModule {
    
    public JacksonMixinModule() {
        setMixInAnnotation(Restaurante.class, RestauranteMixin.class);
        setMixInAnnotation(Cidade.class, CidadeMixin.class);
        setMixInAnnotation(Cozinha.class, CozinhaMixin.class);
    }
}
