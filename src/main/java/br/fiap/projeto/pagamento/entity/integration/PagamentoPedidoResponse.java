package br.fiap.projeto.pagamento.entity.integration;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PagamentoPedidoResponse {
    private String codigoPedido;
    private String status;
    private LocalDateTime dataPagamento;

    public PagamentoPedidoResponse(String codigoPedido, String statusPagamento, LocalDateTime dataPagamento) {
        this.codigoPedido = codigoPedido;
        this.status = statusPagamento;
        this.dataPagamento = dataPagamento;
    }

}
