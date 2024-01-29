package br.fiap.projeto.pagamento.usecase.port.usecase;


import br.fiap.projeto.pagamento.entity.Pagamento;

public interface IProcessaNovoPagamentoUseCase {

    Pagamento criaNovoPagamento(Pagamento pagamento);
}
