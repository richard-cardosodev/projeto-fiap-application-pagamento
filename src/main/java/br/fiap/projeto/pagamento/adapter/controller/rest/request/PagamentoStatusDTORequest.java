package br.fiap.projeto.pagamento.adapter.controller.rest.request;

import br.fiap.projeto.pagamento.entity.enums.StatusPagamento;

public class PagamentoStatusDTORequest {

    private String codigoPedido;

    private StatusPagamento status;

    public PagamentoStatusDTORequest() {
    }

    public PagamentoStatusDTORequest(String codigoPedido, StatusPagamento status) {
        this.codigoPedido = codigoPedido;
        this.status = status;
    }

    public PagamentoStatusDTORequest(String codigoPedido) {
        this.codigoPedido = codigoPedido;
    }

    public String getCodigoPedido() {
        return codigoPedido;
    }

    public void setCodigoPedido(String codigoPedido) {
        this.codigoPedido = codigoPedido;
    }

    public StatusPagamento getStatus() {
        return status;
    }

    public void setStatus(StatusPagamento status) {
        this.status = status;
    }
}
