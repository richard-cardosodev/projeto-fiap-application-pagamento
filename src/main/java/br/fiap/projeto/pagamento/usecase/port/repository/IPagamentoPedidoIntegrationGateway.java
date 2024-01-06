package br.fiap.projeto.pagamento.usecase.port.repository;

import br.fiap.projeto.pagamento.entity.integration.PagamentoPedido;
import br.fiap.projeto.pagamento.entity.integration.PagamentoPedidoResponse;

import java.util.List;

public interface IPagamentoPedidoIntegrationGateway {
    List<PagamentoPedido> buscaPedidosAPagar();

    void atualizaStatusPagamentoPedido(PagamentoPedidoResponse pagamentoPedidoResponse);
}
