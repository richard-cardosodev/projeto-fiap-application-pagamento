package br.fiap.projeto.pagamento.controller;

import br.fiap.projeto.pagamento.adapter.controller.ProcessaNovoPagamentoRestAdapterController;
import br.fiap.projeto.pagamento.adapter.controller.rest.request.PedidoAPagarDTORequest;
import br.fiap.projeto.pagamento.adapter.controller.rest.response.PagamentoNovoDTOResponse;
import br.fiap.projeto.pagamento.adapter.gateway.ProcessaNovoPagamentoRepositoryAdapterGateway;
import br.fiap.projeto.pagamento.entity.Pagamento;
import br.fiap.projeto.pagamento.entity.enums.StatusPagamento;
import br.fiap.projeto.pagamento.external.repository.postgres.SpringPagamentoRepository;
import br.fiap.projeto.pagamento.usecase.port.usecase.IBuscaPagamentoUseCase;
import br.fiap.projeto.pagamento.usecase.port.usecase.IProcessaNovoPagamentoUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ProcessaNovoPagamentoRestAdapterControllerTest {

    @Mock
    private IProcessaNovoPagamentoUseCase processaNovoPagamentoUseCase;

    private ProcessaNovoPagamentoRestAdapterController controller;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        controller = new ProcessaNovoPagamentoRestAdapterController(processaNovoPagamentoUseCase);
    }

    @Test
    public void deveriaCriarUmNovoPagamento() {
        PedidoAPagarDTORequest pedidoRequest = new PedidoAPagarDTORequest();
        Pagamento pagamento = new Pagamento(UUID.randomUUID(), String.valueOf(UUID.randomUUID()), StatusPagamento.APPROVED, new Date(),
                50.89);
        when(processaNovoPagamentoUseCase.criaNovoPagamento(Mockito.any(Pagamento.class)))
                .thenReturn(pagamento);
        PagamentoNovoDTOResponse response = controller.criaNovoPagamento(pedidoRequest);
        Mockito.verify(processaNovoPagamentoUseCase).criaNovoPagamento(Mockito.any(Pagamento.class));
        assertEquals(pagamento.getCodigoPedido(), response.getCodigoPedido());
    }

    @Test
    public void deveriaSalvarUmNovoPagamento() {

        SpringPagamentoRepository mockSpringRepository = Mockito.mock(SpringPagamentoRepository.class);
        IBuscaPagamentoUseCase mockBuscaPagamentoUseCase = Mockito.mock(IBuscaPagamentoUseCase.class);

        ProcessaNovoPagamentoRepositoryAdapterGateway adapterGateway =
                new ProcessaNovoPagamentoRepositoryAdapterGateway(mockSpringRepository, mockBuscaPagamentoUseCase);

        Pagamento pagamento = new Pagamento(UUID.randomUUID(), "0956febf-b719-47fc-83fb-393e5b6275d8", StatusPagamento.APPROVED, new Date(),
                50.89);


        when(mockBuscaPagamentoUseCase.findByCodigoPedido("0956febf-b719-47fc-83fb-393e5b6275d8")).thenReturn(Collections.singletonList(pagamento));

        Pagamento result = adapterGateway.salvaNovoPagamento(pagamento);

        assertEquals(pagamento, result);
    }


}
