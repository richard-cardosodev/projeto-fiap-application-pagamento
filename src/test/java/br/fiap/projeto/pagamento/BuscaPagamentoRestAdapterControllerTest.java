package br.fiap.projeto.pagamento;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import br.fiap.projeto.pagamento.adapter.controller.rest.request.PagamentoDTORequest;
import br.fiap.projeto.pagamento.adapter.controller.rest.request.PagamentoStatusDTORequest;
import br.fiap.projeto.pagamento.adapter.controller.rest.response.PagamentoDTOResponse;
import br.fiap.projeto.pagamento.usecase.port.usecase.IBuscaPagamentoUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.fiap.projeto.pagamento.adapter.controller.BuscaPagamentoRestAdapterController;
import br.fiap.projeto.pagamento.entity.Pagamento;
import br.fiap.projeto.pagamento.entity.enums.StatusPagamento;

public class BuscaPagamentoRestAdapterControllerTest {

    @Mock
    private IBuscaPagamentoUseCase buscaPagamentoUseCase;
    @InjectMocks
    private BuscaPagamentoRestAdapterController controller;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void deveriaBuscarTodosOsPagamentos() {

        List<Pagamento> pagamentos = setupListaDePagamentos();

        Mockito.when(buscaPagamentoUseCase.findAll()).thenReturn(pagamentos);

        List<PagamentoDTOResponse> result = controller.findAll();

        Mockito.verify(buscaPagamentoUseCase, times(1)).findAll();

        assertEquals(pagamentos.size(), result.size());
        assertEquals(pagamentos.get(0).getCodigoPedido(), result.get(0).getCodigoPedido());
    }

    @Test
    public void deveriaBuscarUmPagamentoUsandoOCodigoDoPedido() {
        UUID codigoDoPedido = UUID.randomUUID();

        Pagamento pagamento = new Pagamento(UUID.randomUUID(), String.valueOf(codigoDoPedido), StatusPagamento.APPROVED, new Date(),
                50.89);

        Mockito.when(buscaPagamentoUseCase.findByCodigo(pagamento.getCodigo())).thenReturn(pagamento);

        PagamentoDTOResponse result = controller.findByCodigo(pagamento.getCodigo());

        Mockito.verify(buscaPagamentoUseCase).findByCodigo(pagamento.getCodigo());

        assertEquals(pagamento.getCodigoPedido(), result.getCodigoPedido());
    }

    @Test
    public void deveriaRetornarListaDePagamentosCancelados() {

        List<Pagamento> listaPagamentos = setupListaDePagamentos(StatusPagamento.CANCELLED);

        when(buscaPagamentoUseCase.findByStatusPagamento(any(StatusPagamento.class)))
                .thenReturn(listaPagamentos);

        List<Pagamento> listaBuscaPagamentos = buscaPagamentoUseCase.findByStatusPagamento(StatusPagamento.CANCELLED);

        Mockito.verify(buscaPagamentoUseCase, times(1)).findByStatusPagamento(any(StatusPagamento.class));
        assertNotNull(listaBuscaPagamentos);
        assertEquals(2, listaBuscaPagamentos.size());

    }

    @Test
    public void deveriaRetornarListaDePagamentosAprovados() {

        List<Pagamento> listaPagamentos = setupListaDePagamentos(StatusPagamento.APPROVED);

        when(buscaPagamentoUseCase.findByStatusPagamento(any(StatusPagamento.class)))
                .thenReturn(listaPagamentos);

        List<Pagamento> listaBuscaPagamentos = buscaPagamentoUseCase.findByStatusPagamento(StatusPagamento.APPROVED);

        Mockito.verify(buscaPagamentoUseCase, times(1)).findByStatusPagamento(any(StatusPagamento.class));
        assertNotNull(listaBuscaPagamentos);
        assertEquals(2, listaBuscaPagamentos.size());

    }

    @Test
    public void deveriaRetornarListaDePagamentosRejeitados() {

        List<Pagamento> listaPagamentos = setupListaDePagamentos(StatusPagamento.REJECTED);

        when(buscaPagamentoUseCase.findByStatusPagamento(any(StatusPagamento.class)))
                .thenReturn(listaPagamentos);

        List<Pagamento> listaBuscaPagamentos = buscaPagamentoUseCase.findByStatusPagamento(StatusPagamento.REJECTED);
        Mockito.verify(buscaPagamentoUseCase, times(1)).findByStatusPagamento(any(StatusPagamento.class));
        assertNotNull(listaBuscaPagamentos);
        assertEquals(2, listaBuscaPagamentos.size());

    }

    @Test
    public void deveriaRetornarListaDePagamentosPendentes() {


        List<Pagamento> listaPagamentos = setupListaDePagamentos(StatusPagamento.PENDING);


        when(buscaPagamentoUseCase.findByStatusPagamento(any(StatusPagamento.class)))
                .thenReturn(listaPagamentos);

        List<Pagamento> listaBuscaPagamentos = buscaPagamentoUseCase.findByStatusPagamento(StatusPagamento.PENDING);
        Mockito.verify(buscaPagamentoUseCase, times(1)).findByStatusPagamento(any(StatusPagamento.class));
        assertNotNull(listaBuscaPagamentos);
        assertEquals(2, listaBuscaPagamentos.size());

    }

    @Test
    public void deveriaRetornarListaDePagamentosEmProcessamento() {

        List<Pagamento> listaPagamentos = setupListaDePagamentos(StatusPagamento.IN_PROCESS);

        when(buscaPagamentoUseCase.findByStatusPagamento(any(StatusPagamento.class)))
                .thenReturn(listaPagamentos);

        List<Pagamento> listaBuscaPagamentos = buscaPagamentoUseCase.findByStatusPagamento(StatusPagamento.IN_PROCESS);
        Mockito.verify(buscaPagamentoUseCase, times(1)).findByStatusPagamento(any(StatusPagamento.class));
        assertNotNull(listaBuscaPagamentos);
        assertEquals(2, listaBuscaPagamentos.size());

    }

    private static List<Pagamento> setupListaDePagamentos() {
        List<Pagamento> listaPagamentos = Arrays.asList(

        new Pagamento(UUID.randomUUID(), String.valueOf(UUID.randomUUID()), StatusPagamento.IN_PROCESS, new Date(),
                235.4),
        new Pagamento(UUID.randomUUID(), String.valueOf(UUID.randomUUID()), StatusPagamento.PENDING, new Date(),
                55.7),
        new Pagamento(UUID.randomUUID(), String.valueOf(UUID.randomUUID()), StatusPagamento.APPROVED, new Date(),
                97.12),
        new Pagamento(UUID.randomUUID(), String.valueOf(UUID.randomUUID()), StatusPagamento.REJECTED, new Date(),
                15.27),
        new Pagamento(UUID.randomUUID(), String.valueOf(UUID.randomUUID()), StatusPagamento.CANCELLED, new Date(),
                354.44)
        );

        return listaPagamentos;
    }
    private static List<Pagamento> setupListaDePagamentos(StatusPagamento statusPagamento) {
        List<Pagamento> listaPagamentos = Arrays.asList(
                new Pagamento(UUID.randomUUID(), String.valueOf(UUID.randomUUID()), statusPagamento, new Date(),
                        235.4),
                new Pagamento(UUID.randomUUID(), String.valueOf(UUID.randomUUID()), statusPagamento, new Date(),
                        105.7));
        return listaPagamentos;
    }


}
