package br.fiap.projeto.pagamento.bdd.acceptance.steps.controller;

import br.fiap.projeto.pagamento.adapter.controller.BuscaPagamentoRestAdapterController;
import br.fiap.projeto.pagamento.adapter.controller.ProcessaNovoPagamentoRestAdapterController;
import br.fiap.projeto.pagamento.adapter.controller.rest.request.PedidoAPagarDTORequest;
import br.fiap.projeto.pagamento.usecase.port.usecase.IBuscaPagamentoUseCase;
import br.fiap.projeto.pagamento.usecase.port.usecase.IProcessaNovoPagamentoUseCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.UUID;

public class BuscaPagamentoSteps {

    private final String ENDPOINT_BUSCA_PAGAMENTO = "http://localhost:8080/pagamento/pagamento/busca";

    private final String ENDPOINT_NOVO_PAGAMENTO = "http://localhost:8080/pagamento/pagamento/processa/novo-pagamento";

    private String endpoint;
    private String jsonString;

    private String codigoPedido;
    private String valor;
    private String status;


    //RestAssured
    private RequestSpecification requestSpecification;
    private Response response;




    private IBuscaPagamentoUseCase buscaPagamentoUseCase;
    private IProcessaNovoPagamentoUseCase processaNovoPagamentoUseCase;

     @Given("an unknown order {string} for payment")
    public void an_unknown_order_for_payment(String codigoPedido) {
        endpoint = ENDPOINT_BUSCA_PAGAMENTO+"/por-codigo-pedido/"+codigoPedido;
        System.out.println(endpoint);
        RestAssuredMockMvc.standaloneSetup(new BuscaPagamentoRestAdapterController(buscaPagamentoUseCase));
        requestSpecification = RestAssured.given().contentType(ContentType.JSON);
    }
    @When("attempt to fetch the payment details")
    public void attempt_to_fetch_the_payment_details() {
        response = requestSpecification.when().get(endpoint);
    }
    @Then("the API call should be handled with an error")
    public void the_api_call_should_be_handled_with_an_error() {
        Assert.assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
    }
    @Then("the response should display not found payment details for this order")
    public void the_response_should_display_not_found_payment_details_for_this_order() {
        Assert.assertTrue(response.getBody().asString().contains("Pagamento para este código de pedido não foi encontrado. Verifique o código."));
    }

    @Given("that is required to list all the {string} payments")
    public void that_is_required_to_list_all_the_payments(String status) {
        endpoint = ENDPOINT_BUSCA_PAGAMENTO+"/por-status/"+status;
        System.out.println(endpoint);
        RestAssuredMockMvc.standaloneSetup(new BuscaPagamentoRestAdapterController(buscaPagamentoUseCase));
        requestSpecification = RestAssured.given().contentType(ContentType.JSON);
    }
    @When("searching payments by payment status")
    public void searching_payments_by_payment_status() {
        response = requestSpecification.when().get(endpoint);
    }
    @Then("the API call should return a list of approved payments")
    public void the_api_call_should_return_a_list_of_approved_payments() {
        Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }


    @Given("the payment order with code")
    public void the_payment_order_with_code() throws JsonProcessingException {

        endpoint = ENDPOINT_NOVO_PAGAMENTO;
        RestAssuredMockMvc.standaloneSetup(new ProcessaNovoPagamentoRestAdapterController(processaNovoPagamentoUseCase));
        requestSpecification = RestAssured.given().contentType(ContentType.JSON);

        PedidoAPagarDTORequest requestDTO = setupNewPaymentRequest();

        ObjectMapper objectMapper = new ObjectMapper();
        jsonString = objectMapper.writeValueAsString(requestDTO);

        response = requestSpecification.body(jsonString)
                .when().post(endpoint);


        // Extract codigoPedido value using JSON path
        JsonPath jsonPath = new JsonPath(response.getBody().asString());
        codigoPedido = jsonPath.getString("codigoPedido");
        valor = jsonPath.getString("valorTotal");
        status = jsonPath.getString("status");

        endpoint = ENDPOINT_BUSCA_PAGAMENTO+"/por-codigo-pedido/"+codigoPedido;
        System.out.println(endpoint);
        RestAssuredMockMvc.standaloneSetup(new BuscaPagamentoRestAdapterController(buscaPagamentoUseCase));
        requestSpecification = RestAssured.given().contentType(ContentType.JSON);
    }
    @When("searching the payment by its code")
    public void searching_the_payment_by_its_code() {
        response = requestSpecification.when().get(endpoint);
    }
    @Then("the API should return its details containing the code")
    public void the_api_should_return_its_payment_details() {
        Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        Assert.assertTrue(response.getBody().asString().contains(codigoPedido));
    }


    private static PedidoAPagarDTORequest setupNewPaymentRequest() {
        PedidoAPagarDTORequest requestDTO = new PedidoAPagarDTORequest();
        requestDTO.setDataPagamento(new Date());
        requestDTO.setCodigoPedido(String.valueOf(UUID.randomUUID()));
        requestDTO.setValorTotal(75.0);
        return requestDTO;
    }
}
