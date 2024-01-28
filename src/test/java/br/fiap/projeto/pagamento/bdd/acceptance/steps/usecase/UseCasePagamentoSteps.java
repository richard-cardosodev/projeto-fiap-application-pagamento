package br.fiap.projeto.pagamento.bdd.acceptance.steps.usecase;

import br.fiap.projeto.pagamento.entity.Pagamento;
import br.fiap.projeto.pagamento.entity.enums.StatusPagamento;
import br.fiap.projeto.pagamento.usecase.AtualizaStatusPagamentoUseCase;
import br.fiap.projeto.pagamento.usecase.BuscaPagamentoUseCase;
import br.fiap.projeto.pagamento.usecase.PagamentoPedidoIntegrationUseCase;
import br.fiap.projeto.pagamento.usecase.ProcessaNovoPagamentoUseCase;
import br.fiap.projeto.pagamento.usecase.exceptions.ResourceNotFoundException;
import br.fiap.projeto.pagamento.usecase.port.repository.IAtualizaStatusPagamentoRepositoryAdapterGateway;
import br.fiap.projeto.pagamento.usecase.port.repository.IBuscaPagamentoRepositoryAdapterGateway;
import br.fiap.projeto.pagamento.usecase.port.repository.IProcessaNovoPagamentoRepositoryAdapterGateway;
import br.fiap.projeto.pagamento.usecase.port.usecase.IAtualizaStatusPagamentoUsecase;
import br.fiap.projeto.pagamento.usecase.port.usecase.IBuscaPagamentoUseCase;
import br.fiap.projeto.pagamento.usecase.port.usecase.IPagamentoPedidoIntegrationUseCase;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.UUID;

import static br.fiap.projeto.pagamento.entity.enums.StatusPagamento.PENDING;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UseCasePagamentoSteps {

    @Mock
    private IAtualizaStatusPagamentoRepositoryAdapterGateway atualizaStatusPagamentoRepository;

    @Mock
    private IAtualizaStatusPagamentoUsecase iAtualizaStatusPagamentoUsecase;

    @Mock
    private  IBuscaPagamentoUseCase iBuscaPagamentoUseCase;
    @Mock
    private  IPagamentoPedidoIntegrationUseCase integrationUseCase;

    @Mock
    private IProcessaNovoPagamentoRepositoryAdapterGateway processaNovoPagamentoRepository;


    @Mock
    private IBuscaPagamentoRepositoryAdapterGateway buscaPagamentoRepository;

    @Mock
    private PagamentoPedidoIntegrationUseCase pagamentoPedidoIntegrationUseCase;

    @InjectMocks
    private ProcessaNovoPagamentoUseCase processaNovoPagamentoUseCase;

    @InjectMocks
    private BuscaPagamentoUseCase buscaPagamentoUseCase;

    @InjectMocks
    private AtualizaStatusPagamentoUseCase atualizaStatusPagamentoUseCase;

    private String codigoPedido;
    private UUID codigo;
    StatusPagamento status;

    private Pagamento pagamento;

    @Before
    public void setupMocks(){
        MockitoAnnotations.openMocks(this);
        processaNovoPagamentoRepository.salvaNovoPagamento(pagamento);

    }

    @Given("a new payment order")
    public void a_new_payment_order() {
        MockitoAnnotations.openMocks(this);
        processaNovoPagamentoUseCase = new ProcessaNovoPagamentoUseCase(processaNovoPagamentoRepository, iBuscaPagamentoUseCase);
        Pagamento pagamento = new Pagamento(UUID.randomUUID(), String.valueOf(UUID.randomUUID()), StatusPagamento.PENDING, new Date(), Math.random());

        processaNovoPagamentoUseCase.criaNovoPagamento(pagamento);
        processaNovoPagamentoRepository.salvaNovoPagamento(pagamento);

        codigo = pagamento.getCodigo();
        codigoPedido = pagamento.getCodigoPedido();
        status = pagamento.getStatus();
    }
    @When("attempting to fetch the payment not found")
    public void attempting_to_fetch_the_payment_not_found() {
        atualizaStatusPagamentoUseCase = new AtualizaStatusPagamentoUseCase(atualizaStatusPagamentoRepository, buscaPagamentoUseCase, pagamentoPedidoIntegrationUseCase);

        assertThrows(ResourceNotFoundException.class,() -> {
            atualizaStatusPagamentoUseCase.atualizaStatusPagamento(codigoPedido, StatusPagamento.APPROVED);
        });
    }
    @Then("the payment status should not change its status")
    public void the_payment_status_should_not_change_its_status() {

       assertEquals(status, PENDING);
    }

}


