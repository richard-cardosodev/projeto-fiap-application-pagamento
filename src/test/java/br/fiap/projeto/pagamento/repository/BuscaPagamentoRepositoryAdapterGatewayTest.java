package br.fiap.projeto.pagamento.repository;

import br.fiap.projeto.pagamento.adapter.gateway.BuscaPagamentoRepositoryAdapterGateway;
import br.fiap.projeto.pagamento.entity.Pagamento;
import br.fiap.projeto.pagamento.entity.enums.StatusPagamento;
import br.fiap.projeto.pagamento.external.repository.entity.PagamentoEntity;
import br.fiap.projeto.pagamento.external.repository.postgres.SpringPagamentoRepository;
import br.fiap.projeto.pagamento.usecase.BuscaPagamentoUseCase;
import br.fiap.projeto.pagamento.usecase.exceptions.ResourceNotFoundException;
import br.fiap.projeto.pagamento.usecase.port.repository.IBuscaPagamentoRepositoryAdapterGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BuscaPagamentoRepositoryAdapterGatewayTest {

    @Mock
    private SpringPagamentoRepository springPagamentoRepository;
    @InjectMocks
    private BuscaPagamentoRepositoryAdapterGateway repositoryAdapterGateway;

    private IBuscaPagamentoRepositoryAdapterGateway pagamentoAdapterGateway;
    private BuscaPagamentoUseCase buscaUseCaseMock;

    private String codigoPedido;

    private String codigoPedidoPagamento;

    private PagamentoEntity pagamentoEntity;

    private List<PagamentoEntity> listaPagamentos;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        pagamentoAdapterGateway = mock(IBuscaPagamentoRepositoryAdapterGateway.class);
        buscaUseCaseMock = new BuscaPagamentoUseCase(pagamentoAdapterGateway);

        codigoPedido = "ed83a2b4-5c2d-4ba1-8b6b-3b304035ccbf";

        listaPagamentos = setupListaDePagamentosEntity();
        pagamentoEntity = listaPagamentos.get(1);
        codigoPedidoPagamento = pagamentoEntity.getCodigoPedido();

    }

    @Test
    public void deveriaBuscarTodosOsPagamentos() {

        Mockito.when(springPagamentoRepository.findAll()).thenReturn(listaPagamentos);
        List<Pagamento> result = repositoryAdapterGateway.findAll();
        Mockito.verify(springPagamentoRepository, times(1)).findAll();
        assertEquals(listaPagamentos.size(), result.size());
        assertEquals(listaPagamentos.get(0).getCodigoPedido(), result.get(0).getCodigoPedido());
    }


    @Test
    public void deveriaBuscarUmPagamentoUsandoOCodigoDoPagamento() {

        PagamentoEntity pagamentoEntity = new PagamentoEntity(UUID.randomUUID(), String.valueOf(UUID.randomUUID()), StatusPagamento.APPROVED, new Date(),
                Math.random());
        Mockito.when(springPagamentoRepository.findByCodigo(pagamentoEntity.getCodigo())).thenReturn(pagamentoEntity);
        Pagamento pagamento = repositoryAdapterGateway.findByCodigo(pagamentoEntity.getCodigo());
        Mockito.verify(springPagamentoRepository, times(1)).findByCodigo(pagamento.getCodigo());
        assertEquals(pagamentoEntity.getCodigoPedido(), pagamento.getCodigoPedido());
    }

    @Test
    public void deveriaBuscarUmPagamentoUsandoOCodigoDoPedido() {

        Mockito.when(springPagamentoRepository.findByCodigoPedido(String.valueOf(codigoPedidoPagamento))).thenReturn(
                new ArrayList<>(listaPagamentos));

        List<Pagamento> pagamento = repositoryAdapterGateway.findByCodigoPedido(pagamentoEntity.getCodigoPedido());

        Mockito.verify(springPagamentoRepository, times(1)).findByCodigoPedido(pagamento.get(1).getCodigoPedido());

        assertEquals(pagamentoEntity.getCodigoPedido(), pagamento.get(1).getCodigoPedido());
    }

    @Test
    public void deveriaBuscarUmPagamentoUsandoOCodigoDoPedidoEStatus() {

        Mockito.when(springPagamentoRepository.findByCodigoPedidoAndStatusPagamento(codigoPedidoPagamento, StatusPagamento.APPROVED)).thenReturn(
                new ArrayList<>(listaPagamentos));

        List<Pagamento> pagamentos = repositoryAdapterGateway.findByCodigoPedidoAndStatusPagamento(codigoPedidoPagamento, StatusPagamento.APPROVED);

        Mockito.verify(springPagamentoRepository, times(1)).findByCodigoPedidoAndStatusPagamento(codigoPedidoPagamento, StatusPagamento.APPROVED);

        assertEquals(pagamentoEntity.getCodigoPedido(), pagamentos.get(1).getCodigoPedido());
    }

    @Test
    public void deveriaRetornarListaDePagamentosPorStatus() {

        when(springPagamentoRepository.findByStatusPagamento(any(StatusPagamento.class)))
                .thenReturn(listaPagamentos);

        List<Pagamento> listaBuscaPagamentos = repositoryAdapterGateway.findByStatusPagamento(StatusPagamento.CANCELLED);

        Mockito.verify(springPagamentoRepository, times(1)).findByStatusPagamento(any(StatusPagamento.class));
        assertNotNull(listaBuscaPagamentos);
        assertEquals(listaPagamentos.size(), listaBuscaPagamentos.size());

    }

    @Test
    public void deveriaRetornarListaDePagamentosPorStatusExcetoRejected() {

        when(springPagamentoRepository.findByCodigoPedidoAndStatusPagamentoNot(codigoPedidoPagamento, StatusPagamento.IN_PROCESS))
                .thenReturn(new ArrayList<>(listaPagamentos));

        List<Pagamento> listaBuscaPagamentos = repositoryAdapterGateway.findByCodigoPedidoAndStatusPagamentoNotRejected(codigoPedidoPagamento, StatusPagamento.IN_PROCESS);

        //Simulando Lista filtrada sem Rejecteds
        listaBuscaPagamentos = filterList(listaBuscaPagamentos, StatusPagamento.REJECTED);


        Mockito.verify(springPagamentoRepository, times(1)).findByCodigoPedidoAndStatusPagamentoNot(any(String.class),  any(StatusPagamento.class));
        assertNotNull(listaBuscaPagamentos);
        //se tiver qlqr elemento, assertion falha
        assertFalse(listaBuscaPagamentos.stream().anyMatch(p -> p.getStatus() == StatusPagamento.REJECTED));

    }
    @Test
    public void deveriaRetornarListaDePagamentosPorStatusRejected() {

        List<Pagamento> listaPagamentos = setupListaDePagamentos();

        when(repositoryAdapterGateway.findByCodigoPedidoAndStatusPagamento(codigoPedidoPagamento, StatusPagamento.REJECTED))
                .thenReturn(new ArrayList<>(listaPagamentos));

        List<Pagamento> listaBuscaPagamentos = repositoryAdapterGateway.findByCodigoPedidoAndStatusPagamento(codigoPedidoPagamento, StatusPagamento.IN_PROCESS);

        listaBuscaPagamentos = filterPaymentsByStatus(listaBuscaPagamentos, StatusPagamento.REJECTED);

        Mockito.verify(springPagamentoRepository, times(1)).findByCodigoPedidoAndStatusPagamento(any(String.class),  any(StatusPagamento.class));
        assertNotNull(listaBuscaPagamentos);

        assertFalse(listaBuscaPagamentos.stream().anyMatch(p -> p.getStatus() == StatusPagamento.IN_PROCESS));
        assertFalse(listaBuscaPagamentos.stream().anyMatch(p -> p.getStatus() == StatusPagamento.PENDING));
        assertFalse(listaBuscaPagamentos.stream().anyMatch(p -> p.getStatus() == StatusPagamento.APPROVED));
        assertFalse(listaBuscaPagamentos.stream().anyMatch(p -> p.getStatus() == StatusPagamento.CANCELLED));

    }

    //TODO verificar esse comportamento
    @Test
    public void deveriaRetornarPagamentosFiltrandoNaoRejeitados() {

        Pagamento nonRejectedPayment =  new Pagamento(UUID.randomUUID(), codigoPedido, StatusPagamento.APPROVED, new Date(),31.22);
        Pagamento rejectedPayment =  new Pagamento(UUID.randomUUID(), "d8dc5531-25d9-4690-9636-07e5e419bc83", StatusPagamento.REJECTED, new Date(),35.4);


        List<Pagamento> pagamentoList = Arrays.asList(rejectedPayment, nonRejectedPayment);

        when(pagamentoAdapterGateway.findByCodigoPedidoAndStatusPagamentoNotRejected(codigoPedido, StatusPagamento.REJECTED))
                .thenReturn(pagamentoList);

         buscaUseCaseMock.findByCodigoPedidoNotRejected(codigoPedido);
        verify(pagamentoAdapterGateway).findByCodigoPedidoAndStatusPagamentoNotRejected(codigoPedido, StatusPagamento.REJECTED);

        assertNotNull(nonRejectedPayment);
        assertEquals(nonRejectedPayment, nonRejectedPayment);
    }

    @Test
    public void deveriaLancarExcecaoQuandoNaoEncontrarResultadosBuscaPorCodigoNaoRejeitados() {
        
        assertThrows(ResourceNotFoundException.class, () -> {
            when(pagamentoAdapterGateway.findByCodigoPedidoAndStatusPagamentoNotRejected(codigoPedido, StatusPagamento.APPROVED))
                    .thenReturn(Collections.emptyList());

            buscaUseCaseMock.findByCodigoPedidoNotRejected(codigoPedido);
            verify(pagamentoAdapterGateway).findByCodigoPedidoAndStatusPagamentoNotRejected(codigoPedido, StatusPagamento.APPROVED);
        });

    }

    @Test
    public void deveriaLancarExcecaoQuandoNaoEncontrarResultadosBuscaPorCodigoRejeitados() {

        assertThrows(ResourceNotFoundException.class, () -> {

            when(pagamentoAdapterGateway.findByCodigoPedidoAndStatusPagamento(codigoPedido, StatusPagamento.REJECTED))
                    .thenReturn(Collections.emptyList());
            buscaUseCaseMock.findByCodigoPedidoRejected(codigoPedido);
            verify(pagamentoAdapterGateway).findByCodigoPedidoAndStatusPagamento(codigoPedido, StatusPagamento.REJECTED);
        });
    }
        @Test
        public void deveriaLancarExcecaoQuandoNaoEncontrarResultadosBuscaPorCodigoPendentes() {
        
            assertThrows(ResourceNotFoundException.class, () -> {

                when(pagamentoAdapterGateway.findByCodigoPedidoAndStatusPagamento(codigoPedido, StatusPagamento.PENDING))
                        .thenReturn(Collections.emptyList());
                buscaUseCaseMock.findByCodigoPedidoPending(codigoPedido);
                verify(pagamentoAdapterGateway).findByCodigoPedidoAndStatusPagamento(codigoPedido, StatusPagamento.PENDING);
            });
    }

    @Test
    public void deveriaLancarExcecaoQuandoNaoEncontrarResultadosBuscaPorCodigoEmProcessamento() {

        assertThrows(ResourceNotFoundException.class, () -> {

            when(pagamentoAdapterGateway.findByCodigoPedidoAndStatusPagamento(codigoPedido, StatusPagamento.IN_PROCESS))
                    .thenReturn(Collections.emptyList());
            buscaUseCaseMock.findByCodigoPedidoPending(codigoPedido);
            verify(pagamentoAdapterGateway).findByCodigoPedidoAndStatusPagamento(codigoPedido, StatusPagamento.IN_PROCESS);
        });
    }

    @Test
    public void deveriaLancarExcecaoQuandoNaoEncontrarResultadosBuscaPorCodigo() {

        assertThrows(ResourceNotFoundException.class, () -> {

            when(pagamentoAdapterGateway.findByCodigoPedido(codigoPedido))
                    .thenReturn(null);
            buscaUseCaseMock.findByCodigoPedido(codigoPedido);
            verify(pagamentoAdapterGateway).findByCodigoPedido(codigoPedido);
        });
    }

    @Test
    public void deveriaRetornarUmaListaDePagamentoPorStatus() {

        List<Pagamento> listaPagamentos = setupListaDePagamentos();
        listaPagamentos = filterPaymentsByStatus(listaPagamentos, StatusPagamento.APPROVED);

            when(pagamentoAdapterGateway.findByStatusPagamento(StatusPagamento.APPROVED))
                    .thenReturn(listaPagamentos);
            buscaUseCaseMock.findByStatusPagamento(StatusPagamento.APPROVED);
            verify(pagamentoAdapterGateway).findByStatusPagamento(StatusPagamento.APPROVED);

        assertFalse(listaPagamentos.stream().anyMatch(p -> p.getStatus() == StatusPagamento.REJECTED));

    }

    private static List<PagamentoEntity> setupListaDePagamentosEntity() {
        List<PagamentoEntity> listaPagamentos = Arrays.asList(

        new PagamentoEntity(UUID.randomUUID(), "d8dc5531-25d9-4690-9636-07e5e419bc83", StatusPagamento.IN_PROCESS, new Date(),
                235.4),
        new PagamentoEntity(UUID.randomUUID(), "41d2375c-15ee-4274-bc41-04dea2a118b4", StatusPagamento.PENDING, new Date(),
                55.7),
        new PagamentoEntity(UUID.randomUUID(), "c20cf05a-39a0-4f38-89b6-dc55bcb8e1e5", StatusPagamento.APPROVED, new Date(),
                97.12),
        new PagamentoEntity(UUID.randomUUID(), "da3f92a2-ee12-4ec1-805d-c889584a3e4d", StatusPagamento.REJECTED, new Date(),
                15.27),
        new PagamentoEntity(UUID.randomUUID(), "07ed9681-fa92-40db-843c-33739149e864", StatusPagamento.CANCELLED, new Date(),
                354.44),
        new PagamentoEntity(UUID.randomUUID(), "e8f9c900-fe52-417c-b53c-d298ace56277", StatusPagamento.APPROVED, new Date(),
                14.12),
        new PagamentoEntity(UUID.randomUUID(), "54368936-c0b7-483f-aa25-4ffa7ec79270", StatusPagamento.APPROVED, new Date(),
                50.13)
        );

        return listaPagamentos;
    }

    private static List<Pagamento> setupListaDePagamentos() {
        List<Pagamento> listaPagamentos = Arrays.asList(

                new Pagamento(UUID.randomUUID(), "d8dc5531-25d9-4690-9636-07e5e419bc83", StatusPagamento.IN_PROCESS, new Date(),
                        235.4),
                new Pagamento(UUID.randomUUID(), "41d2375c-15ee-4274-bc41-04dea2a118b4", StatusPagamento.PENDING, new Date(),
                        55.7),
                new Pagamento(UUID.randomUUID(), "c20cf05a-39a0-4f38-89b6-dc55bcb8e1e5", StatusPagamento.APPROVED, new Date(),
                        97.12),
                new Pagamento(UUID.randomUUID(), "da3f92a2-ee12-4ec1-805d-c889584a3e4d", StatusPagamento.REJECTED, new Date(),
                        15.27),
                new Pagamento(UUID.randomUUID(), "07ed9681-fa92-40db-843c-33739149e864", StatusPagamento.CANCELLED, new Date(),
                        354.44),
                new Pagamento(UUID.randomUUID(), "e8f9c900-fe52-417c-b53c-d298ace56277", StatusPagamento.APPROVED, new Date(),
                        14.12),
                new Pagamento(UUID.randomUUID(), "54368936-c0b7-483f-aa25-4ffa7ec79270", StatusPagamento.APPROVED, new Date(),
                        50.13)
        );

        return listaPagamentos;
    }

    private List<Pagamento> filterList(List<Pagamento> listaBuscaPagamentos, StatusPagamento statusPagamento) {
        return listaBuscaPagamentos.stream().filter(pagamento -> !statusPagamento.equals(pagamento.getStatus())).collect(Collectors.toList());
    }

    private List<Pagamento> filterPaymentsByStatus(List<Pagamento> listaBuscaPagamentos, StatusPagamento status) {
        return listaBuscaPagamentos.stream().filter(pagamento -> pagamento.getStatus() == status).collect(Collectors.toList());
    }

}
