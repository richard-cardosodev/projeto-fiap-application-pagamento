package br.fiap.projeto.pagamento.adapter.controller.rest.request;

import br.fiap.projeto.pagamento.entity.enums.StatusPagamento;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class PagamentoStatusDTORequest {

    private String codigoPedido;

    private StatusPagamento status;

    public PagamentoStatusDTORequest(String codigoPedido, StatusPagamento status) {
        this.codigoPedido = codigoPedido;
        this.status = status;
    }

    public PagamentoStatusDTORequest(String codigoPedido) {
        this.codigoPedido = codigoPedido;
    }
}
