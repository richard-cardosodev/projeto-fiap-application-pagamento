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

}
