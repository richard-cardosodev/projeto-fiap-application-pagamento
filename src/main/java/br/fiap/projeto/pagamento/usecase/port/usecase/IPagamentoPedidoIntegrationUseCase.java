package br.fiap.projeto.pagamento.usecase.port.usecase;

import br.fiap.projeto.pagamento.entity.Pagamento;

public interface IPagamentoPedidoIntegrationUseCase {

    void atualizarPagamentoPedido(Pagamento codigoPedido);

    void scheduleAtualizaPagamentoPedido(String codigo);

    void shutDownScheduler();


}
