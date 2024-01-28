package br.fiap.projeto.pagamento.adapter.controller.rest.response;

import br.fiap.projeto.pagamento.entity.Pagamento;
import br.fiap.projeto.pagamento.entity.enums.StatusPagamento;

public class PagamentoNovoDTOResponse {

    private String codigoPedido;

    private StatusPagamento status;


    public PagamentoNovoDTOResponse(Pagamento pagamento) {
        this.setCodigoPedido(pagamento.getCodigoPedido());
        this.setStatus(pagamento.getStatus());
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
