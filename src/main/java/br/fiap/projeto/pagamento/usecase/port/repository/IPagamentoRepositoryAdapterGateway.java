package br.fiap.projeto.pagamento.usecase.port.repository;

import br.fiap.projeto.pagamento.entity.Pagamento;

public interface IPagamentoRepositoryAdapterGateway {

    //TODO Refatorar após testes - usado apenas no DataLoader
     void salvaPagamento(Pagamento pagamento);




}
