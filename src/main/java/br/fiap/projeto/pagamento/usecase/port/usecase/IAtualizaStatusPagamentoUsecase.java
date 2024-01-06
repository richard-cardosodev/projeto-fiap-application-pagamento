package br.fiap.projeto.pagamento.usecase.port.usecase;

import br.fiap.projeto.pagamento.entity.enums.StatusPagamento;

public interface IAtualizaStatusPagamentoUsecase {
    //TODO verificar a necessidade de devolver o pagamento atualizado na ResponseEntity
    void atualizaStatusPagamento(String codigoPedido, StatusPagamento statusPagamento);

    void atualizaStatusPagamentoGateway(String codigoPedido, StatusPagamento statusPagamento);
}
