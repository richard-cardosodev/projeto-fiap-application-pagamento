package br.fiap.projeto.pagamento.adapter.gateway;

import br.fiap.projeto.pagamento.entity.Pagamento;
import br.fiap.projeto.pagamento.external.repository.entity.PagamentoEntity;
import br.fiap.projeto.pagamento.external.repository.postgres.SpringPagamentoRepository;
import br.fiap.projeto.pagamento.usecase.port.repository.IProcessaNovoPagamentoRepositoryAdapterGateway;
import br.fiap.projeto.pagamento.usecase.port.usecase.IBuscaPagamentoUseCase;

public class ProcessaNovoPagamentoRepositoryAdapterGateway implements IProcessaNovoPagamentoRepositoryAdapterGateway {

    private final SpringPagamentoRepository springPagamentoRepository;
    private final IBuscaPagamentoUseCase buscaPagamentoUseCase;

    public ProcessaNovoPagamentoRepositoryAdapterGateway(SpringPagamentoRepository springPagamentoRepository, IBuscaPagamentoUseCase buscaPagamentoUseCase) {
        this.springPagamentoRepository = springPagamentoRepository;
        this.buscaPagamentoUseCase = buscaPagamentoUseCase;
    }

    @Override
    public Pagamento salvaNovoPagamento(Pagamento pagamento) {
        springPagamentoRepository.save(new PagamentoEntity(pagamento));
        return buscaPagamentoUseCase
                .findByCodigoPedido(pagamento.getCodigoPedido())
                .stream()
                .filter(p -> p.getCodigoPedido()
                        .equals(pagamento.getCodigoPedido())).findFirst().get();
    }
}
