package br.fiap.projeto.pagamento.external.integration.port;

import lombok.Data;

@Data
public class PagamentoPedidoResponseDTO {
    private String codigoPedido;
    private String statusPagamento;

    public PagamentoPedidoResponseDTO(String codigoPedido, String statusPagamento) {
        this.codigoPedido = codigoPedido;
        this.statusPagamento = statusPagamento;
    }
}
