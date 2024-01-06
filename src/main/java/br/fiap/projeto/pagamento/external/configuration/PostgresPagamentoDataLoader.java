package br.fiap.projeto.pagamento.external.configuration;

import br.fiap.projeto.pagamento.entity.Pagamento;
import br.fiap.projeto.pagamento.entity.enums.StatusPagamento;
import br.fiap.projeto.pagamento.external.repository.postgres.PostgresIPagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Configuration
public class PostgresPagamentoDataLoader {

    @Autowired
    private PostgresIPagamentoRepository pagamentoRepository;

    @PostConstruct
    public void init(){
        List<Pagamento> pagamentoList = Arrays.asList(
                new Pagamento(UUID.randomUUID(), "bfbf7088-99a2-4b17-ba31-c514fad6e4c4", StatusPagamento.IN_PROCESS, new Date(), 25.0),
                new Pagamento(UUID.randomUUID(), "ef406be7-a698-4131-acdf-2950b65e7ea0", StatusPagamento.IN_PROCESS, new Date(), 55.0),
                new Pagamento(UUID.randomUUID(), "a29d6696-038e-49e6-a1b0-83f8f892f607", StatusPagamento.APPROVED, new Date(), 785.0),
                new Pagamento(UUID.randomUUID(), "811ae18b-715c-42eb-a611-65fbe8363c23", StatusPagamento.APPROVED, new Date(), 100.0),
                new Pagamento(UUID.randomUUID(), "332bb60c-9b10-4445-b1c3-2dc865a5c894", StatusPagamento.REJECTED, new Date(), 10.0),
                new Pagamento(UUID.randomUUID(), "238897af-66f0-4342-a6e7-012a1e3498b6", StatusPagamento.APPROVED, new Date(), 41.0),
                new Pagamento(UUID.randomUUID(), "41507c7b-25ca-485f-8b12-3ee1d826531b", StatusPagamento.REJECTED, new Date(),25.0),
                new Pagamento(UUID.randomUUID(), "f24e88ec-625f-463b-ba52-d7f7ee28c38a", StatusPagamento.APPROVED, new Date(), 45.0),
                new Pagamento(UUID.randomUUID(), "6b02dbda-a759-4985-98a1-a3f57217ce9a", StatusPagamento.REJECTED, new Date(),22.0),
                new Pagamento(UUID.randomUUID(), "990e1501-ffc5-44e4-beda-53a4ec033daa", StatusPagamento.REJECTED, new Date(),10.0),
                new Pagamento(UUID.randomUUID(), "79c2f361-71d2-44bb-a7a7-04bf775dc29a", StatusPagamento.CANCELLED, new Date(), 15.0),
                new Pagamento(UUID.randomUUID(), "f15d9fd5-2d7b-4869-a19c-7ed2c6f1b471", StatusPagamento.CANCELLED, new Date(), 13.50),
                new Pagamento(UUID.randomUUID(), "f4f35c49-f1ef-483d-b6e1-fb2f0a5edf1e", StatusPagamento.PENDING, new Date(),45.0),
                new Pagamento(UUID.randomUUID(), "0ac866ba-e880-4b76-bb18-1c2300bf08a8", StatusPagamento.PENDING, new Date(), 223.0)
                );
        pagamentoList.forEach(pagamento -> pagamentoRepository.salvaPagamento(pagamento));
    }

}
