package br.fiap.projeto.pagamento.adapter.gateway;

import br.fiap.projeto.pagamento.entity.Pagamento;
import br.fiap.projeto.pagamento.external.repository.entity.PagamentoEntity;
import br.fiap.projeto.pagamento.external.repository.postgres.SpringPagamentoRepository;
import br.fiap.projeto.pagamento.usecase.port.repository.IAtualizaStatusPagamentoRepositoryAdapterGateway;


public class AtualizaStatusPagamentoRepositoryAdapterGateway implements IAtualizaStatusPagamentoRepositoryAdapterGateway {

    private final SpringPagamentoRepository springPagamentoRepository;

    public AtualizaStatusPagamentoRepositoryAdapterGateway(SpringPagamentoRepository springPagamentoRepository) {
        this.springPagamentoRepository = springPagamentoRepository;
    }

    public void atualizaStatusPagamento(Pagamento pagamento){
        PagamentoEntity pagamentoStatusAtualizado = new PagamentoEntity(pagamento);
        this.springPagamentoRepository.save(pagamentoStatusAtualizado);
    }
}
