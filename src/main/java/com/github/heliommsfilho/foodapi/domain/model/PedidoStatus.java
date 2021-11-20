package com.github.heliommsfilho.foodapi.domain.model;

import java.util.Arrays;
import java.util.List;

public enum PedidoStatus {

    CRIADO("Criado"),
    CONFIRMADO("Confirmado", CRIADO),
    ENTREGUE("Entregue", CONFIRMADO),
    CANCELADO("Cancelado", CRIADO, CONFIRMADO);
    
    private String descricao;
    private List<PedidoStatus> statusAnteriores;
    
    PedidoStatus(String descricao, PedidoStatus... statusAnteriores) {
        this.descricao = descricao;
        this.statusAnteriores = Arrays.asList(statusAnteriores);
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public boolean naoPodeAlterarPara(final PedidoStatus novoStatus) {
        return !novoStatus.statusAnteriores.contains(this);
    }
}
