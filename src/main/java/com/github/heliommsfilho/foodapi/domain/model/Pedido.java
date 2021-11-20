package com.github.heliommsfilho.foodapi.domain.model;

import com.github.heliommsfilho.foodapi.domain.exception.NegocioException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Pedido {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String codigo;

    private BigDecimal subtotal;
    private BigDecimal taxaFrete;
    private BigDecimal valorTotal;

    @Embedded
    private Endereco enderecoEntrega;

    @Enumerated(EnumType.STRING)
    private PedidoStatus status = PedidoStatus.CRIADO;

    @CreationTimestamp
    private OffsetDateTime dataCriacao;

    private OffsetDateTime dataConfirmacao;
    private OffsetDateTime dataCancelamento;
    private OffsetDateTime dataEntrega;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private FormaPagamento formaPagamento;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Restaurante restaurante;

    @ManyToOne
    @JoinColumn(name = "usuario_cliente_id", nullable = false)
    private Usuario cliente;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<ItemPedido> itens = new ArrayList<>();
    
    @PrePersist
    private void gerarCodigo() {
        setCodigo(UUID.randomUUID().toString());
    }
    
    public void calcularValorTotal() {
        getItens().forEach(ItemPedido::calcularPrecoTotal);
        
        this.subtotal = getItens().stream()
                .map(item -> item.getPrecoTotal())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        this.valorTotal = this.subtotal.add(this.taxaFrete);
    }
    
    public void definirFrete() {
        setTaxaFrete(getRestaurante().getTaxaFrete());
    }
    
    public void atribuirPedidoAosItens() {
        getItens().forEach(item -> item.setPedido(this));
    }
    
    public void confirmar() {
        setStatus(PedidoStatus.CONFIRMADO);
        setDataConfirmacao(OffsetDateTime.now());
    }
    
    public void entregar() {
        setStatus(PedidoStatus.ENTREGUE);
        setDataEntrega(OffsetDateTime.now());
    }
    
    public void cancelar() {
        setStatus(PedidoStatus.CANCELADO);
        setDataCancelamento(OffsetDateTime.now());
    }
    
    private void setStatus(PedidoStatus novoStatus) {
        
        if (getStatus().naoPodeAlterarPara(novoStatus)) {
            throw new NegocioException(String.format("Status do pedido %s não pode ser alterado de %s para %s.",
                                                     getCodigo(), getStatus().getDescricao(), novoStatus.getDescricao()));
        }
        
        this.status = novoStatus;
    }
}
