package com.github.heliommsfilho.foodapi.domain.service;

import com.github.heliommsfilho.foodapi.domain.filter.VendaDiariaFilter;
import com.github.heliommsfilho.foodapi.domain.model.dto.VendaDiaria;

import java.util.List;

public interface VendaQueryService {
    
    List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter filtro);
    
}
