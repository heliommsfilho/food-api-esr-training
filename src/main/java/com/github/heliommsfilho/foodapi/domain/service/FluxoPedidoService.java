package com.github.heliommsfilho.foodapi.domain.service;

import com.github.heliommsfilho.foodapi.domain.model.Pedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FluxoPedidoService {
    
    @Autowired
    private EmissaoPedidoService emissaoPedido;
    
    @Transactional
    public void confirmar(final String codigoPedido) {
        final Pedido pedido = emissaoPedido.buscarOuFalhar(codigoPedido);
        pedido.confirmar();
    }
    
    @Transactional
    public void cancelar(String codigoPedido) {
        Pedido pedido = emissaoPedido.buscarOuFalhar(codigoPedido);
        pedido.cancelar();
    }
    
    @Transactional
    public void entregar(String codigoPedido) {
        Pedido pedido = emissaoPedido.buscarOuFalhar(codigoPedido);
        pedido.entregar();
    }
}
