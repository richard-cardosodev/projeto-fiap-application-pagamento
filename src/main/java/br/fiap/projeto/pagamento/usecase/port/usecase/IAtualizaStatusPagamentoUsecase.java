package br.fiap.projeto.pagamento.usecase.port.usecase;

import br.fiap.projeto.pagamento.entity.enums.StatusPagamento;
import br.fiap.projeto.pagamento.usecase.exceptions.JsonProcessingException;

public interface IAtualizaStatusPagamentoUsecase {

    void atualizaStatusPagamento(String codigoPedido, StatusPagamento statusPagamento) throws JsonProcessingException;

    void atualizaStatusPagamentoGateway(String codigoPedido, StatusPagamento statusPagamento);
}
