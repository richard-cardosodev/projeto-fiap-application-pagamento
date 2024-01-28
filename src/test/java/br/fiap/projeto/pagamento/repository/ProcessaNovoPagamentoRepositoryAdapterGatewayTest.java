package br.fiap.projeto.pagamento.repository;

import br.fiap.projeto.pagamento.entity.Pagamento;
import br.fiap.projeto.pagamento.entity.enums.StatusPagamento;
import br.fiap.projeto.pagamento.usecase.ProcessaNovoPagamentoUseCase;
import br.fiap.projeto.pagamento.usecase.exceptions.ResourceAlreadyInProcessException;
import br.fiap.projeto.pagamento.usecase.port.repository.IProcessaNovoPagamentoRepositoryAdapterGateway;
import br.fiap.projeto.pagamento.usecase.port.usecase.IBuscaPagamentoUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ProcessaNovoPagamentoRepositoryAdapterGatewayTest {

    private IProcessaNovoPagamentoRepositoryAdapterGateway mockProcessaNovoPagamentoAdapterGateway;

    private IBuscaPagamentoUseCase mockBuscaPagamentoUseCase;

    private ProcessaNovoPagamentoUseCase processaNovoPagamentoUseCase;

    private Pagamento pagamento;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockProcessaNovoPagamentoAdapterGateway = mock(IProcessaNovoPagamentoRepositoryAdapterGateway.class);
        mockBuscaPagamentoUseCase = mock(IBuscaPagamentoUseCase.class);
        processaNovoPagamentoUseCase = new ProcessaNovoPagamentoUseCase(mockProcessaNovoPagamentoAdapterGateway, mockBuscaPagamentoUseCase);
        pagamento = new Pagamento(UUID.randomUUID(), "d8dc5531-25d9-4690-9636-07e5e419bc83", StatusPagamento.APPROVED, new Date(),35.4);
    }

    @Test
    void deveriaLancarExcecaoAoTentarCriarUmPagamentoSeNaoForPossivelPagar() {

        when(processaNovoPagamentoUseCase.isPossivelPagar(pagamento.getCodigoPedido())).thenThrow(ResourceAlreadyInProcessException.class);
        assertThrows(ResourceAlreadyInProcessException.class, () -> {
            processaNovoPagamentoUseCase.criaNovoPagamento(pagamento);
        });
        verify(mockProcessaNovoPagamentoAdapterGateway, never()).salvaNovoPagamento(any());
    }

}