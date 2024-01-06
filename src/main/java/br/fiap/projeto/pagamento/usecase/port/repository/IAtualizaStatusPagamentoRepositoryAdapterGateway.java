package br.fiap.projeto.pagamento.usecase.port.repository;


import br.fiap.projeto.pagamento.entity.Pagamento;



public interface IAtualizaStatusPagamentoRepositoryAdapterGateway {

    void atualizaStatusPagamento(Pagamento pagamento);

}
