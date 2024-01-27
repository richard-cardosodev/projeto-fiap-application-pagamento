package br.fiap.projeto.pagamento;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;
import java.util.UUID;

import br.fiap.projeto.pagamento.adapter.controller.rest.request.PedidoAPagarDTORequest;
import br.fiap.projeto.pagamento.adapter.controller.rest.response.PagamentoNovoDTOResponse;
import br.fiap.projeto.pagamento.usecase.port.usecase.IProcessaNovoPagamentoUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.fiap.projeto.pagamento.adapter.controller.ProcessaNovoPagamentoRestAdapterController;
import br.fiap.projeto.pagamento.entity.Pagamento;
import br.fiap.projeto.pagamento.entity.enums.StatusPagamento;

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
        Pagamento pagamento = new Pagamento(UUID.randomUUID(), String.valueOf(UUID.randomUUID()), StatusPagamento.APPROVED, new Date(1234454),
                50.89);
        Mockito.when(processaNovoPagamentoUseCase.criaNovoPagamento(Mockito.any(Pagamento.class)))
                .thenReturn(pagamento);
        PagamentoNovoDTOResponse response = controller.criaNovoPagamento(pedidoRequest);
        Mockito.verify(processaNovoPagamentoUseCase).criaNovoPagamento(Mockito.any(Pagamento.class));
        assertEquals(pagamento.getCodigoPedido(), response.getCodigoPedido());

    }
}
