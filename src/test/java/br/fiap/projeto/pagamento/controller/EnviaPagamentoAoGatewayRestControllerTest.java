package br.fiap.projeto.pagamento.controller;

import br.fiap.projeto.pagamento.adapter.controller.EnviaPagamentoAoGatewayRestAdapterController;
import br.fiap.projeto.pagamento.adapter.controller.rest.request.PagamentoAEnviarAoGatewayDTORequest;
import br.fiap.projeto.pagamento.adapter.gateway.BuscaPagamentoRepositoryAdapterGateway;
import br.fiap.projeto.pagamento.entity.Pagamento;
import br.fiap.projeto.pagamento.entity.enums.StatusPagamento;
import br.fiap.projeto.pagamento.usecase.BuscaPagamentoUseCase;
import br.fiap.projeto.pagamento.usecase.EnviaPagamentoAoGatewayPagamentosUseCase;
import br.fiap.projeto.pagamento.usecase.exceptions.ResourceNotFoundException;
import br.fiap.projeto.pagamento.usecase.port.usecase.IBuscaPagamentoUseCase;
import br.fiap.projeto.pagamento.usecase.port.usecase.IEnviaPagamentoAoGatewayPagamentosUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

class EnviaPagamentoAoGatewayRestControllerTest {

    @InjectMocks
    private EnviaPagamentoAoGatewayRestAdapterController enviaPagamentoAoGatewayRestAdapterController;

    @Mock
    private IEnviaPagamentoAoGatewayPagamentosUseCase iEnviaPagamentoAoGatewayPagamentosUseCase;

    @InjectMocks
    private EnviaPagamentoAoGatewayPagamentosUseCase enviaPagamentoAoGatewayPagamentosUseCase;



    @Mock
    private BuscaPagamentoRepositoryAdapterGateway buscaPagamentoRepositoryAdapterGateway;

    @Mock
    private IBuscaPagamentoUseCase iBuscaPagamentoUseCase;
    @InjectMocks
    private BuscaPagamentoUseCase buscaPagamentoUseCase;


    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveriaPrepararRequestParaEnviarPagamentoValidoAoGatewayDePagamentosController(){

        PagamentoAEnviarAoGatewayDTORequest requestGatewayDTO = setupRequestToExternalGateway();
        Pagamento pagamento = mock(Pagamento.class);

        Mockito.when(iEnviaPagamentoAoGatewayPagamentosUseCase.preparaParaEnviarPagamentoAoGateway(anyString())).thenReturn(pagamento);
        enviaPagamentoAoGatewayRestAdapterController.preparaParaEnviarPagamentoAoGateway(requestGatewayDTO);

        Mockito.verify(iEnviaPagamentoAoGatewayPagamentosUseCase, times(1))
                .preparaParaEnviarPagamentoAoGateway(requestGatewayDTO.getCodigoPedido());

        enviaPagamentoAoGatewayRestAdapterController.enviaParaGatewayDePagamento(requestGatewayDTO);
        Mockito.verify(iEnviaPagamentoAoGatewayPagamentosUseCase, times(1))
                .enviaRequestAoSistemaExternoPagamentos(requestGatewayDTO.getCodigoPedido(), requestGatewayDTO.getStatusPagamento());

    }

    @Test
    void deveriaPrepararRequestParaAtualizarStatusDePagamentosUseCase(){

        Pagamento pagamento = new Pagamento(UUID.randomUUID(), String.valueOf(UUID.randomUUID()), StatusPagamento.IN_PROCESS, new Date(), 55.47);

        assertThrows(NullPointerException.class, () -> {
            Mockito.when(iEnviaPagamentoAoGatewayPagamentosUseCase.preparaParaEnviarPagamentoAoGateway(anyString())).thenReturn(pagamento);
            Pagamento result = enviaPagamentoAoGatewayPagamentosUseCase.preparaParaEnviarPagamentoAoGateway(pagamento.getCodigoPedido());
            Mockito.verify(iEnviaPagamentoAoGatewayPagamentosUseCase, times(1)).preparaParaEnviarPagamentoAoGateway(pagamento.getCodigoPedido());
            assertNull(result);
        });

    }

    private PagamentoAEnviarAoGatewayDTORequest setupRequestToExternalGateway() {
        PagamentoAEnviarAoGatewayDTORequest requestDTO = new PagamentoAEnviarAoGatewayDTORequest();
        requestDTO.setValorTotal(55.0);
        requestDTO.setCodigoPedido("b1f33cab-24f0-40ef-8a47-e73557f6a9f5");
        requestDTO.setDataPagamento(new Date());
        requestDTO.setStatusPagamento(StatusPagamento.PENDING);
        return requestDTO;
    }
}
