package br.fiap.projeto.pagamento.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import br.fiap.projeto.pagamento.adapter.controller.rest.request.PagamentoDTORequest;
import br.fiap.projeto.pagamento.adapter.controller.rest.request.PagamentoStatusDTORequest;
import br.fiap.projeto.pagamento.adapter.gateway.AtualizaStatusPagamentoRepositoryAdapterGateway;
import br.fiap.projeto.pagamento.usecase.AtualizaStatusPagamentoUseCase;
import br.fiap.projeto.pagamento.usecase.BuscaPagamentoUseCase;
import br.fiap.projeto.pagamento.usecase.PagamentoPedidoIntegrationUseCase;
import br.fiap.projeto.pagamento.usecase.exceptions.UnprocessablePaymentException;
import br.fiap.projeto.pagamento.usecase.port.usecase.IAtualizaStatusPagamentoUsecase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.fiap.projeto.pagamento.adapter.controller.AtualizaStatusPagamentoRestAdapterController;
import br.fiap.projeto.pagamento.adapter.gateway.BuscaPagamentoRepositoryAdapterGateway;
import br.fiap.projeto.pagamento.entity.Pagamento;
import br.fiap.projeto.pagamento.entity.enums.StatusPagamento;

public class AtualizaStatusPagamentoRestAdapterControllerTest {

    @InjectMocks
    private AtualizaStatusPagamentoRestAdapterController atualizaStatusPagamentoRestAdapterController;
    @InjectMocks
    private BuscaPagamentoUseCase buscaPagamentoUseCase;

    @Mock
    private IAtualizaStatusPagamentoUsecase atualizaStatusPagamentoUsecase;
    @Mock
    private BuscaPagamentoRepositoryAdapterGateway buscaPagamentoAdapterGateway;

    private PagamentoStatusDTORequest pagamentoAtualizado;

    private AtualizaStatusPagamentoRepositoryAdapterGateway atualizaStatusPagamentoAdapterGateway;

    @InjectMocks
    private PagamentoPedidoIntegrationUseCase pedidoIntegrationUseCase;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        buscaPagamentoUseCase = new BuscaPagamentoUseCase(buscaPagamentoAdapterGateway);
    }

    @Test
    public void deveriaAtualizarStatusDoPagamentoPendenteParaEmProcessamento() {

        pagamentoAtualizado = new PagamentoStatusDTORequest(String.valueOf(UUID.randomUUID()), StatusPagamento.IN_PROCESS);

        atualizaStatusPagamentoRestAdapterController.atualizaStatusPagamento(new PagamentoDTORequest(pagamentoAtualizado));

        Mockito.verify(atualizaStatusPagamentoUsecase).atualizaStatusPagamento(pagamentoAtualizado.getCodigoPedido(),
                StatusPagamento.IN_PROCESS);
    }

    @Test
    public void deveriaAtualizarStatusDoPagamentoEmProcessamentoParaAprovado(){
        //3A Arrange, Act, Assert
        pagamentoAtualizado = new PagamentoStatusDTORequest(String.valueOf(UUID.randomUUID()), StatusPagamento.APPROVED);

        atualizaStatusPagamentoRestAdapterController.atualizaStatusPagamento(new PagamentoDTORequest(pagamentoAtualizado));

        Mockito.verify(atualizaStatusPagamentoUsecase).atualizaStatusPagamento(pagamentoAtualizado.getCodigoPedido(),
                StatusPagamento.APPROVED);

    }

    @Test
    public void deveriaAtualizarStatusDoPagamentoEmProcessamentoParaCancelado(){

        pagamentoAtualizado = new PagamentoStatusDTORequest(String.valueOf(UUID.randomUUID()), StatusPagamento.CANCELLED);

        atualizaStatusPagamentoRestAdapterController.atualizaStatusPagamento(new PagamentoDTORequest(pagamentoAtualizado));

        Mockito.verify(atualizaStatusPagamentoUsecase).atualizaStatusPagamento(pagamentoAtualizado.getCodigoPedido(),
                StatusPagamento.CANCELLED);

    }

    @Test
    public void deveriaAtualizarStatusDoPagamentoEmProcessamentoParaRejeitado(){

        pagamentoAtualizado = new PagamentoStatusDTORequest(String.valueOf(UUID.randomUUID()), StatusPagamento.REJECTED);

        atualizaStatusPagamentoRestAdapterController.atualizaStatusPagamento(new PagamentoDTORequest(pagamentoAtualizado));

        Mockito.verify(atualizaStatusPagamentoUsecase).atualizaStatusPagamento(pagamentoAtualizado.getCodigoPedido(),
                StatusPagamento.REJECTED);

    }

    @Test
    public void deveriaLancarExcecaoAoAtualizarStatusParaStatusInvalido(){

        pagamentoAtualizado = new PagamentoStatusDTORequest(String.valueOf(UUID.randomUUID()), StatusPagamento.PENDING);

        atualizaStatusPagamentoRestAdapterController.atualizaStatusPagamento(new PagamentoDTORequest(pagamentoAtualizado));

        Mockito.verify(atualizaStatusPagamentoUsecase).atualizaStatusPagamento(pagamentoAtualizado.getCodigoPedido(),
                StatusPagamento.PENDING);
        Mockito.doThrow(UnprocessablePaymentException.class)
                .when(atualizaStatusPagamentoUsecase)
                .atualizaStatusPagamento(pagamentoAtualizado.getCodigoPedido(), pagamentoAtualizado.getStatus());

        assertThrows(UnprocessablePaymentException.class, () ->{
            atualizaStatusPagamentoRestAdapterController.atualizaStatusPagamento(new PagamentoDTORequest(pagamentoAtualizado));
        });
    }

    @Test
    public void deveriaLancarExcecaoTransicaoInvalidaEntreEstadosEmProcessamento() {
        String codigoPedido = "f4f35c49-f1ef-483d-b6e1-fb2f0a5edf1e";
        StatusPagamento statusPagamento = StatusPagamento.IN_PROCESS;
        Pagamento pagamento = new Pagamento(UUID.randomUUID(), String.valueOf(UUID.randomUUID()), statusPagamento, new Date(), 55.47);

        BuscaPagamentoUseCase buscaPagamentoUseCaseMockado = mock(BuscaPagamentoUseCase.class);
        when(buscaPagamentoUseCaseMockado.findByCodigoPedidoPending(codigoPedido)).thenReturn(pagamento);

        AtualizaStatusPagamentoUseCase atualizaStatusPagamentoUseCase = new AtualizaStatusPagamentoUseCase(
                atualizaStatusPagamentoAdapterGateway,
                buscaPagamentoUseCaseMockado,
                pedidoIntegrationUseCase
        );
        assertThrows(UnprocessablePaymentException.class, () -> {
            atualizaStatusPagamentoUseCase.atualizaStatusPagamentoGateway(codigoPedido, statusPagamento);
            verify(atualizaStatusPagamentoAdapterGateway, times(1)).atualizaStatusPagamento(pagamento);
        });

    }
}
