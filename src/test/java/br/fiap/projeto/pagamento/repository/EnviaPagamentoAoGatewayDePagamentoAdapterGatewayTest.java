package br.fiap.projeto.pagamento.repository;

import br.fiap.projeto.pagamento.entity.Pagamento;
import br.fiap.projeto.pagamento.entity.enums.StatusPagamento;
import br.fiap.projeto.pagamento.usecase.EnviaPagamentoAoGatewayPagamentosUseCase;
import br.fiap.projeto.pagamento.usecase.exceptions.ResourceNotFoundException;
import br.fiap.projeto.pagamento.usecase.port.usecase.IAtualizaStatusPagamentoUsecase;
import br.fiap.projeto.pagamento.usecase.port.usecase.IBuscaPagamentoUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


class EnviaPagamentoAoGatewayDePagamentoAdapterGatewayTest {


    private IBuscaPagamentoUseCase buscaPagamentoUseCaseMockado;


    private IAtualizaStatusPagamentoUsecase atualizaStatusPagamentoUsecaseMockado;


    private EnviaPagamentoAoGatewayPagamentosUseCase enviaPagamentoAoGatewayPagamentosUseCase;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        buscaPagamentoUseCaseMockado = mock(IBuscaPagamentoUseCase.class);
        atualizaStatusPagamentoUsecaseMockado = mock(IAtualizaStatusPagamentoUsecase.class);
        enviaPagamentoAoGatewayPagamentosUseCase = new EnviaPagamentoAoGatewayPagamentosUseCase(
              buscaPagamentoUseCaseMockado, atualizaStatusPagamentoUsecaseMockado);
    }

    @Test
    void deveriaPrepararOPagamentoParaEnviarAGatewayComSucesso() {

        Pagamento pagamento = new Pagamento(UUID.randomUUID(), "d8dc5531-25d9-4690-9636-07e5e419bc83", StatusPagamento.PENDING, new Date(),35.4);
        when(buscaPagamentoUseCaseMockado.findByCodigoPedidoPending(anyString())).thenReturn(pagamento);

        enviaPagamentoAoGatewayPagamentosUseCase.preparaParaEnviarPagamentoAoGateway(pagamento.getCodigoPedido());

        verify(buscaPagamentoUseCaseMockado, times(3)).findByCodigoPedidoPending(anyString());
        verify(atualizaStatusPagamentoUsecaseMockado, times(1)).atualizaStatusPagamentoGateway(anyString(), eq(StatusPagamento.IN_PROCESS));
    }

    @Test
    void deveriaLancarUmaExcecaoAntesDeEnviarGatewayPorNaoAcharOPagamento() {

        when(buscaPagamentoUseCaseMockado.findByCodigoPedidoPending(anyString())).thenThrow(new NoSuchElementException(""));

        assertThrows(ResourceNotFoundException.class, () -> {
            enviaPagamentoAoGatewayPagamentosUseCase.preparaParaEnviarPagamentoAoGateway("5999747d-a1b6-438b-b0c6-612a87f844fc");
        });

        verify(buscaPagamentoUseCaseMockado, times(1)).findByCodigoPedidoPending(anyString());
        verify(atualizaStatusPagamentoUsecaseMockado, never()).atualizaStatusPagamentoGateway(anyString(), eq(StatusPagamento.IN_PROCESS));
    }


}
