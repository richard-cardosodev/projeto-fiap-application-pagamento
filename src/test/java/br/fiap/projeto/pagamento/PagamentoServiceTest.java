package br.fiap.projeto.pagamento;

import br.fiap.projeto.pagamento.adapter.gateway.BuscaPagamentoRepositoryAdapterGateway;
import br.fiap.projeto.pagamento.entity.Pagamento;
import br.fiap.projeto.pagamento.entity.enums.StatusPagamento;
import br.fiap.projeto.pagamento.usecase.BuscaPagamentoUseCase;
import br.fiap.projeto.pagamento.usecase.ProcessaNovoPagamentoUseCase;
import br.fiap.projeto.pagamento.usecase.exceptions.ResourceNotFoundException;
import br.fiap.projeto.pagamento.usecase.port.repository.IProcessaNovoPagamentoRepositoryAdapterGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PagamentoServiceTest {

        @InjectMocks
        private BuscaPagamentoUseCase buscaPagamentoUseCase;

        private ProcessaNovoPagamentoUseCase novoPagamentoUseCase;
        @Mock
        private BuscaPagamentoRepositoryAdapterGateway buscaPagamentoAdapterGateway;

        @Mock
        private IProcessaNovoPagamentoRepositoryAdapterGateway processaNovoPagamentoAdapterGateway;

        Pagamento pagamento;

        @BeforeEach
        public void setUp() {
                MockitoAnnotations.initMocks(this);
                novoPagamentoUseCase = new ProcessaNovoPagamentoUseCase(processaNovoPagamentoAdapterGateway,
                        buscaPagamentoUseCase);
                buscaPagamentoUseCase = new BuscaPagamentoUseCase(buscaPagamentoAdapterGateway);
        }


        @Test
        void deveriaRetornarUmPagamentoAoBuscarPorCodigoValido() {
                UUID codigo = UUID.randomUUID();

                pagamento = new Pagamento(codigo, String.valueOf(UUID.randomUUID()), StatusPagamento.APPROVED, new Date(), 55.41);

                Mockito.when(buscaPagamentoAdapterGateway.findByCodigo(codigo))
                        .thenReturn(pagamento);

                Pagamento pagamentoEncontrado = buscaPagamentoUseCase.findByCodigo(codigo);

                assertNotNull(pagamentoEncontrado);
                assertEquals(pagamento, pagamentoEncontrado);

        }

        @Test
        void deveriaLancarExecaoAoBuscarUmPagamentoComCodigoInexistente() {
                UUID codigo = UUID.randomUUID();
                assertThrows(ResourceNotFoundException.class, () -> {
                        buscaPagamentoUseCase.findByCodigo(codigo);
                });

        }

        @Test
        void deveriaRetornarTodosOsPagamentos() {

                List<Pagamento> listaDePagamentosMockados = setupListaDePagamentos();

                buscaPagamentoUseCase = new BuscaPagamentoUseCase(buscaPagamentoAdapterGateway);

                Mockito.when(buscaPagamentoAdapterGateway.findAll())
                                .thenReturn(listaDePagamentosMockados);

                List<Pagamento> listaDePagamentosEncontrados = buscaPagamentoUseCase.findAll();

                assertNotNull(listaDePagamentosEncontrados);
                assertEquals(listaDePagamentosMockados, listaDePagamentosEncontrados);

        }

        @Test
        void deveriaCriarUmNovoPagamentoSeForPossivelPagar() {
                UUID codigo = UUID.randomUUID();
                pagamento = new Pagamento(codigo, String.valueOf(UUID.randomUUID()), StatusPagamento.APPROVED, new Date(), 75.51);

                Pagamento possivelPagamento = novoPagamentoUseCase.criaNovoPagamento(pagamento);

                assertNotNull(possivelPagamento);
                assertEquals(pagamento, possivelPagamento);
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

}
