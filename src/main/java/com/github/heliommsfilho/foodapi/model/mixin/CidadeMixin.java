package com.github.heliommsfilho.foodapi.model.mixin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.heliommsfilho.foodapi.domain.model.Estado;

public abstract class CidadeMixin {
    
    @JsonIgnoreProperties(value = "nome", allowGetters = true)
    private Estado estado;
}
