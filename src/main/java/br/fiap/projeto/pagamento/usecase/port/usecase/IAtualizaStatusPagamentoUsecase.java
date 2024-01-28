package br.fiap.projeto.pagamento.usecase.port.usecase;

import br.fiap.projeto.pagamento.entity.enums.StatusPagamento;

public interface IAtualizaStatusPagamentoUsecase {

    void atualizaStatusPagamento(String codigoPedido, StatusPagamento statusPagamento);

    void atualizaStatusPagamentoGateway(String codigoPedido, StatusPagamento statusPagamento);
}
