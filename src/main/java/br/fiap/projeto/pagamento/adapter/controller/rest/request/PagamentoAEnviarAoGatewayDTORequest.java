package br.fiap.projeto.pagamento.adapter.controller.rest.request;

import br.fiap.projeto.pagamento.entity.Pagamento;
import br.fiap.projeto.pagamento.entity.enums.StatusPagamento;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data @NoArgsConstructor
public class PagamentoAEnviarAoGatewayDTORequest {

    private String codigoPedido;
    private Double valorTotal;
    private StatusPagamento statusPagamento;
    private Date dataPagamento;

    public PagamentoAEnviarAoGatewayDTORequest(Pagamento pagamento){
        this.setCodigoPedido(pagamento.getCodigoPedido());
        this.setValorTotal(pagamento.getValorTotal());
        this.setStatusPagamento(pagamento.getStatus());
    }
}
