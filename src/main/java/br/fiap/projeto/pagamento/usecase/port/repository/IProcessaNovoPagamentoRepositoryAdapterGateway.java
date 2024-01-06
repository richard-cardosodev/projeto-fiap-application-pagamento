package br.fiap.projeto.pagamento.usecase.port.repository;

import br.fiap.projeto.pagamento.entity.Pagamento;

public interface IProcessaNovoPagamentoRepositoryAdapterGateway {

    Pagamento salvaNovoPagamento(Pagamento pagamento);
}
