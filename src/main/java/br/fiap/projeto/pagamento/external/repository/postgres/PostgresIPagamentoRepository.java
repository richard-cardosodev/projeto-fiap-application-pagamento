package br.fiap.projeto.pagamento.external.repository.postgres;

import br.fiap.projeto.pagamento.entity.Pagamento;
import br.fiap.projeto.pagamento.external.repository.entity.PagamentoEntity;
import br.fiap.projeto.pagamento.usecase.port.repository.IPagamentoRepositoryAdapterGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;


@Component
@Primary
public class PostgresIPagamentoRepository implements IPagamentoRepositoryAdapterGateway {

    private final SpringPagamentoRepository springPagamentoRepository;

    @Autowired
    public PostgresIPagamentoRepository(SpringPagamentoRepository springPagamentoRepository) {
        this.springPagamentoRepository = springPagamentoRepository;
    }

    @Override
    public void salvaPagamento(Pagamento pagamento) {
        springPagamentoRepository.save(new PagamentoEntity(pagamento));
    }

}
