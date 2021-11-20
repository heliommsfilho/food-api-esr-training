package com.github.heliommsfilho.foodapi.controller;

import com.github.heliommsfilho.foodapi.domain.service.FluxoPedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/pedidos/{codigoPedido}")
public class FluxoPedidoController {
    
    @Autowired
    private FluxoPedidoService fluxoPedido;
    
    @PutMapping("/confirmacao")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void confirmar(@PathVariable final String codigoPedido) {
        fluxoPedido.confirmar(codigoPedido);
    }
    
    @PutMapping("/cancelamento")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelar(@PathVariable String codigoPedido) {
        fluxoPedido.cancelar(codigoPedido);
    }
    
    @PutMapping("/entrega")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void entregar(@PathVariable String codigoPedido) {
        fluxoPedido.entregar(codigoPedido);
    }
}
