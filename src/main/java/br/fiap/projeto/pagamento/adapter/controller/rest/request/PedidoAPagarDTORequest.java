package br.fiap.projeto.pagamento.adapter.controller.rest.request;

import br.fiap.projeto.pagamento.entity.Pagamento;
import br.fiap.projeto.pagamento.external.integration.port.Pedido;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data @NoArgsConstructor
public class PedidoAPagarDTORequest {

    private String codigoPedido;
    private Double valorTotal;
    private Date dataPagamento;
    private List<Pedido> pedidos;

    public PedidoAPagarDTORequest(String codigoPedido, Double valorTotal) {
        this.codigoPedido = codigoPedido;
        this.valorTotal = valorTotal;
    }

    public PedidoAPagarDTORequest(PagamentoDTORequest pagamentoDTO){
        this.setCodigoPedido(pagamentoDTO.getCodigoPedido());
        this.setDataPagamento(pagamentoDTO.getDataPagamento());
    }

    public PedidoAPagarDTORequest(List<Pedido> pedidos) {
        this.pedidos = pedidos;
        for(Pedido pedido : pedidos){
            this.setCodigoPedido(pedido.getCodigo());
            this.setValorTotal(pedido.getValorTotal());
        }
    }
    public PedidoAPagarDTORequest(Pagamento pagamento){
        this.setCodigoPedido(pagamento.getCodigoPedido());
        this.setDataPagamento(pagamento.getDataPagamento());

    }

    //INFO usado na criação de um Novo Pagamento
    public Pagamento conversorDePedidoAPagarDTOParaPagamento(){
        return new Pagamento(codigoPedido, valorTotal);
    }
}
