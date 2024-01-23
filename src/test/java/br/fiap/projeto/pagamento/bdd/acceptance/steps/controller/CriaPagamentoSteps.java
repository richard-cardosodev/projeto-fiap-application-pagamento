package br.fiap.projeto.pagamento.bdd.acceptance.steps.controller;

import br.fiap.projeto.pagamento.adapter.controller.AtualizaStatusPagamentoRestAdapterController;
import br.fiap.projeto.pagamento.adapter.controller.BuscaPagamentoRestAdapterController;
import br.fiap.projeto.pagamento.adapter.controller.EnviaPagamentoAoGatewayRestAdapterController;
import br.fiap.projeto.pagamento.adapter.controller.ProcessaNovoPagamentoRestAdapterController;
import br.fiap.projeto.pagamento.adapter.controller.rest.request.PagamentoAEnviarAoGatewayDTORequest;
import br.fiap.projeto.pagamento.adapter.controller.rest.request.PagamentoDTORequest;
import br.fiap.projeto.pagamento.adapter.controller.rest.request.PedidoAPagarDTORequest;
import br.fiap.projeto.pagamento.entity.enums.StatusPagamento;
import br.fiap.projeto.pagamento.usecase.port.usecase.IAtualizaStatusPagamentoUsecase;
import br.fiap.projeto.pagamento.usecase.port.usecase.IBuscaPagamentoUseCase;
import br.fiap.projeto.pagamento.usecase.port.usecase.IEnviaPagamentoAoGatewayPagamentosUseCase;
import br.fiap.projeto.pagamento.usecase.port.usecase.IProcessaNovoPagamentoUseCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
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

public class CriaPagamentoSteps {

    private final String ENDPOINT_NOVO_PAGAMENTO = "http://localhost:8080/pagamento/pagamento/processa/novo-pagamento";
    private final String ENDPOINT_BUSCA_PAGAMENTO = "http://localhost:8080/pagamento/pagamento/busca";
    private final String ENDPOINT_ENVIA_GATEWAY = "http://localhost:8080/pagamento/pagamento/gateway/gateway-de-pagamento";
    private final String ENDPOINT_RETORNO_GATEWAY = "http://localhost:8080/pagamento/pagamento/retorno-gateway/atualiza-status";


    private String endpoint;

    private String jsonString;

    private String codigoPedido;
    private String valor;
    private String status;



    //RestAssured
    private RequestSpecification requestSpecification;
    private Response response;

    //Dependencia Controller
    private IProcessaNovoPagamentoUseCase processaNovoPagamentoUseCase;

    private IBuscaPagamentoUseCase buscaPagamentoUseCase;

    private IAtualizaStatusPagamentoUsecase atualizaStatusPagamentoUsecase;

    private IEnviaPagamentoAoGatewayPagamentosUseCase enviaPagamentoAoGatewayPagamentosUseCase;




    @Given("the new order arrived for payment")
    public void the_new_order_arrived_for_payment() {
        System.out.println("Chamada do endpoint: /novo-pagamento");
        endpoint = ENDPOINT_NOVO_PAGAMENTO;
        System.out.println(endpoint);
        RestAssuredMockMvc.standaloneSetup(new ProcessaNovoPagamentoRestAdapterController(processaNovoPagamentoUseCase));
        requestSpecification = RestAssured.given().contentType(ContentType.JSON);
    }

    @When("the customer initiates the payment order")
    public void the_customer_initiates_the_payment_order() throws JsonProcessingException {
        PedidoAPagarDTORequest requestDTO = setupNewPaymentRequest();

        ObjectMapper objectMapper = new ObjectMapper();
        jsonString = objectMapper.writeValueAsString(requestDTO);

    }

    @Then("the payment service should receive a request to create a payment order")
    public void the_payment_service_should_receive_a_request_to_create_a_payment_order() {
        response = requestSpecification.body(jsonString)
                .when().post(endpoint);

    }
    @And("the payment order should successfully create the payment order")
    public void the_payment_order_should_succesfully_create_payment() {
        Assert.assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
        Assert.assertTrue(response.getBody().asString().contains("codigoPedido"));
    }

    @Given("the new order arrived for payment with errors")
    public void the_new_order_arrived_for_payment_with_errors() {
        System.out.println("Chamada do endpoint: /novo-pagamento");
        endpoint = ENDPOINT_NOVO_PAGAMENTO;
        System.out.println(endpoint);
        RestAssuredMockMvc.standaloneSetup(new ProcessaNovoPagamentoRestAdapterController(processaNovoPagamentoUseCase));
        requestSpecification = RestAssured.given().contentType(ContentType.JSON);
    }
    @When("the customer initiates the payment order without fixing errors")
    public void the_customer_initiates_the_payment_order_without_fixing_errors() throws JsonProcessingException {
        PedidoAPagarDTORequest requestDTO = setupNewPaymentRequest();
        requestDTO.setCodigoPedido(null);
        ObjectMapper objectMapper = new ObjectMapper();
        jsonString = objectMapper.writeValueAsString(requestDTO);
    }
    @Then("the payment service should receive a request in attempt to create order")
    public void the_payment_service_should_receive_a_request_in_attempt_to_create_order() {
        response = requestSpecification.body(jsonString)
                .when().post(endpoint);
    }
    @And("the payment order should reject the creation of the payment order")
    public void the_payment_order_should_reject_the_creation_of_the_payment_order() {
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
    }

    @Given("the new order {string} for payment")
    public void the_new_order_for_payment(String codigoPedido) {
        System.out.println("Chamada do endpoint: /por-codigo-pedido");
        endpoint = ENDPOINT_BUSCA_PAGAMENTO+"/por-codigo-pedido/"+codigoPedido;
        System.out.println(endpoint);
        RestAssuredMockMvc.standaloneSetup(new BuscaPagamentoRestAdapterController(buscaPagamentoUseCase));
        requestSpecification = RestAssured.given().contentType(ContentType.JSON);

    }
    @When("fetching payment details")
    public void fetching_payment_details() {
        response = requestSpecification.when().get(endpoint);

    }
    @Then("the API call should be handled successfully")
    public void api_call_handled_successfuly() {
        Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCode());

    }

    @And("the response should contain the payment details")
    public void the_response_contains_payment_details() {
        Assert.assertTrue(response.getBody().asString().contains("PENDING"));
    }


    @Given("a new payment order is created for an order")
    public void a_new_payment_order_is_created_for_an_order() throws JsonProcessingException {
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

    }
    @When("the api calls the endpoint to send to payment gateway")
    public void the_api_calls_the_endpoint_to_send_to_payment_gateway() throws JsonProcessingException {
        endpoint = ENDPOINT_ENVIA_GATEWAY;
        System.out.println(endpoint);
        RestAssuredMockMvc.standaloneSetup(new EnviaPagamentoAoGatewayRestAdapterController(enviaPagamentoAoGatewayPagamentosUseCase));
        requestSpecification = RestAssured.given().contentType(ContentType.JSON);

        PagamentoAEnviarAoGatewayDTORequest requestDTO = setupRequestToExternalGateway();
        ObjectMapper objectMapper = new ObjectMapper();
        jsonString = objectMapper.writeValueAsString(requestDTO);

        response = requestSpecification.body(jsonString)
                .when().post(endpoint);

    }

    @Then("the payment order should update its payment details from pending to in process")
    public void the_payment_order_should_update_its_payment_details_from_pending_to_in_proccess() {
        //buscar pra ver se alterou no banco
        endpoint = ENDPOINT_BUSCA_PAGAMENTO+"/por-codigo-pedido/"+codigoPedido;
        System.out.println(endpoint);
        RestAssuredMockMvc.standaloneSetup(new BuscaPagamentoRestAdapterController(buscaPagamentoUseCase));
        requestSpecification = RestAssured.given().contentType(ContentType.JSON);
        response = requestSpecification.when().get(endpoint);
        Assert.assertTrue(response.getBody().asString().contains("IN_PROCESS"));


    }
    @Then("once the payment is approved the payment order should update its status to approved")
    public void once_the_payment_is_approved_the_payment_order_should_update_its_status_to_approved() throws JsonProcessingException {
        endpoint = ENDPOINT_RETORNO_GATEWAY;
        RestAssuredMockMvc.standaloneSetup(new AtualizaStatusPagamentoRestAdapterController(atualizaStatusPagamentoUsecase));
        requestSpecification = RestAssured.given().contentType(ContentType.JSON);

        PagamentoDTORequest requestDTO = setupPagamentoDTORequest();

        ObjectMapper objectMapper = new ObjectMapper();
        jsonString = objectMapper.writeValueAsString(requestDTO);

        response = requestSpecification.body(jsonString)
                .when().patch(endpoint);


        Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCode());

        endpoint = ENDPOINT_BUSCA_PAGAMENTO+"/por-codigo-pedido/"+codigoPedido;
        System.out.println(endpoint);
        RestAssuredMockMvc.standaloneSetup(new BuscaPagamentoRestAdapterController(buscaPagamentoUseCase));
        requestSpecification = RestAssured.given().contentType(ContentType.JSON);
        response = requestSpecification.when().get(endpoint);
        Assert.assertTrue(response.getBody().asString().contains("APPROVED"));


    }



    private PagamentoDTORequest setupPagamentoDTORequest() {
        PagamentoDTORequest requestDTO = new PagamentoDTORequest();
        requestDTO.setCodigoPedido(codigoPedido);
        requestDTO.setStatus(StatusPagamento.APPROVED);
        return requestDTO;
    }

    private PagamentoAEnviarAoGatewayDTORequest setupRequestToExternalGateway() {
        PagamentoAEnviarAoGatewayDTORequest requestDTO = new PagamentoAEnviarAoGatewayDTORequest();
        requestDTO.setValorTotal(55.0);
        requestDTO.setCodigoPedido(codigoPedido);
        requestDTO.setDataPagamento(new Date());
        requestDTO.setStatusPagamento(StatusPagamento.valueOf(status));
        return requestDTO;
    }

    private static PedidoAPagarDTORequest setupNewPaymentRequest() {
        PedidoAPagarDTORequest requestDTO = new PedidoAPagarDTORequest();
        requestDTO.setDataPagamento(new Date());
        requestDTO.setCodigoPedido(String.valueOf(UUID.randomUUID()));
        requestDTO.setValorTotal(55.0);
        return requestDTO;
    }



}
