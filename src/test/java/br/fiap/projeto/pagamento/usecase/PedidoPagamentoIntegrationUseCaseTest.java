package br.fiap.projeto.pagamento.usecase;

import br.fiap.projeto.pagamento.entity.Pagamento;
import br.fiap.projeto.pagamento.entity.enums.StatusPagamento;
import br.fiap.projeto.pagamento.entity.integration.PagamentoPedidoResponse;
import br.fiap.projeto.pagamento.usecase.port.repository.IPagamentoPedidoIntegrationGateway;
import br.fiap.projeto.pagamento.usecase.port.usecase.IBuscaPagamentoUseCase;
import feign.FeignException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;
import java.util.concurrent.ScheduledExecutorService;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class PedidoPagamentoIntegrationUseCaseTest {
    @Mock
    private IPagamentoPedidoIntegrationGateway pagamentoPedidoIntegrationGateway;

    @Mock
    private IBuscaPagamentoUseCase buscaPagamentoUseCase;

    @InjectMocks
    private PagamentoPedidoIntegrationUseCase pagamentoPedidoIntegrationUseCase;

    private ScheduledExecutorService scheduler;
    private String codigoPedido;
    private Pagamento pagamento;

    List<Pagamento> pagamentoList;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        pagamento = new Pagamento(UUID.randomUUID(), "d8dc5531-25d9-4690-9636-07e5e419bc83", StatusPagamento.PENDING, new Date(),35.4);
        codigoPedido = pagamento.getCodigoPedido();

        pagamentoList = new ArrayList<>();
        pagamentoList.add(pagamento);


    }

    @AfterEach
    public void tearDown() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdownNow();
        }
    }


    @Test
    public void deveriaLancarExcecaoAoTentarBuscarUmPagamentoInexistenteParaAtualizarStatus() {

        when(buscaPagamentoUseCase.findByCodigoPedido(codigoPedido)).thenReturn(pagamentoList).thenThrow(NoSuchElementException.class);
        assertThrows(NoSuchElementException.class, () -> {
            pagamentoPedidoIntegrationUseCase.scheduleAtualizaPagamentoPedido(codigoPedido);
        });

    }

    @Test
    public void deveriaLancarUmaExcecaoDaIntegracaoAoTentarBuscarUmPagamentoInexistenteParaAtualizarStatus() {

        when(buscaPagamentoUseCase.findByCodigoPedido(codigoPedido)).thenReturn(pagamentoList).thenThrow(FeignException.class);
        doThrow(FeignException.class).when(pagamentoPedidoIntegrationGateway).atualizaStatusPagamentoPedido(any(PagamentoPedidoResponse.class));
        assertThrows(NoSuchElementException.class, () -> {
            pagamentoPedidoIntegrationUseCase.scheduleAtualizaPagamentoPedido(codigoPedido);
        });
    }

    @Test
    public void deveriaAtualizarOStatusDoPagamentoViaSchedulerIntegration() {

        IPagamentoPedidoIntegrationGateway pagamentoPedidoIntegrationGatewayMock = mock(IPagamentoPedidoIntegrationGateway.class);
        IBuscaPagamentoUseCase buscaPagamentoUseCaseMock = mock(IBuscaPagamentoUseCase.class);

        PagamentoPedidoIntegrationUseCase pagamentoPedidoIntegrationUseCase = new PagamentoPedidoIntegrationUseCase(
                pagamentoPedidoIntegrationGatewayMock, buscaPagamentoUseCaseMock);

        String codigoPedido = pagamento.getCodigoPedido(); // provide a valid code

        List<Pagamento> pagamentos = Arrays.asList(
                new Pagamento("91a032ce-8c0d-4574-9f10-76be33e5f148", StatusPagamento.APPROVED),
                new Pagamento("32d61e8d-fb5f-4d3b-add2-727b765740ee", StatusPagamento.CANCELLED)
        );

        when(buscaPagamentoUseCaseMock.findByCodigoPedido(codigoPedido)).thenReturn(pagamentos);

        assertDoesNotThrow(() -> pagamentoPedidoIntegrationUseCase.scheduleAtualizaPagamentoPedido(codigoPedido));


    }


}
