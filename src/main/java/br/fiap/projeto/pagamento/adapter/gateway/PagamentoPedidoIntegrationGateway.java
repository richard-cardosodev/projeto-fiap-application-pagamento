package br.fiap.projeto.pagamento.adapter.gateway;

import br.fiap.projeto.pagamento.adapter.mapper.PagamentoPedidoMapper;
import br.fiap.projeto.pagamento.entity.integration.PagamentoPedido;
import br.fiap.projeto.pagamento.entity.integration.PagamentoPedidoResponse;
import br.fiap.projeto.pagamento.external.integration.IPagamentoPedidoIntegration;
import br.fiap.projeto.pagamento.external.integration.IPedidoIntegration;
import br.fiap.projeto.pagamento.usecase.port.repository.IPagamentoPedidoIntegrationGateway;

import java.util.List;
import java.util.stream.Collectors;

public class PagamentoPedidoIntegrationGateway implements IPagamentoPedidoIntegrationGateway {
    private final IPedidoIntegration pedidoIntegration;
    private final IPagamentoPedidoIntegration pagamentoPedidoIntegration;

    public PagamentoPedidoIntegrationGateway(IPedidoIntegration pedidoIntegration, IPagamentoPedidoIntegration pagamentoPedidoIntegration) {
        this.pedidoIntegration = pedidoIntegration;
        this.pagamentoPedidoIntegration = pagamentoPedidoIntegration;
    }

    @Override
    public List<PagamentoPedido> buscaPedidosAPagar() {
        return pedidoIntegration.buscaPedidosAPagar().stream().map(PagamentoPedidoMapper::toPagamentoPedido).collect(Collectors.toList());
    }

    @Override
    public void atualizaStatusPagamentoPedido(PagamentoPedidoResponse pagamentoPedidoResponse) {
         pagamentoPedidoIntegration.atualizaStatusPagamentoPedido(pagamentoPedidoResponse);
    }
}