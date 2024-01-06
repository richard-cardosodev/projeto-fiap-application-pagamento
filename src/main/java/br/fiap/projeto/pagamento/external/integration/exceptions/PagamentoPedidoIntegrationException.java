package br.fiap.projeto.pagamento.external.integration.exceptions;

public class PagamentoPedidoIntegrationException extends RuntimeException {
    public PagamentoPedidoIntegrationException(String msg) {
        super(msg);
    }
}
