package com.github.heliommsfilho.foodapi.controller;

import com.github.heliommsfilho.foodapi.domain.filter.VendaDiariaFilter;
import com.github.heliommsfilho.foodapi.domain.model.dto.VendaDiaria;
import com.github.heliommsfilho.foodapi.domain.service.VendaQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/estatisticas")
public class EstatisticasController {
    
    private VendaQueryService vendaQueryService;
    
    @Autowired
    public EstatisticasController(final VendaQueryService vendaQueryService) {
        this.vendaQueryService = vendaQueryService;
    }
    
    @GetMapping("/vendas-diarias")
    public List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter filtro) {
        return vendaQueryService.consultarVendasDiarias(filtro);
    }
}
