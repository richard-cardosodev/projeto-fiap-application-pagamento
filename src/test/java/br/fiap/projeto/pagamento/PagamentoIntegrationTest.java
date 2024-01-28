package br.fiap.projeto.pagamento;

import br.fiap.projeto.pagamento.adapter.controller.BuscaPagamentoRestAdapterController;
import br.fiap.projeto.pagamento.adapter.controller.rest.request.PagamentoAEnviarAoGatewayDTORequest;
import br.fiap.projeto.pagamento.adapter.gateway.BuscaPagamentoRepositoryAdapterGateway;
import br.fiap.projeto.pagamento.entity.Pagamento;
import br.fiap.projeto.pagamento.entity.enums.StatusPagamento;
import br.fiap.projeto.pagamento.external.integration.IPagamentoPedidoIntegration;
import br.fiap.projeto.pagamento.external.integration.IPedidoIntegration;
import br.fiap.projeto.pagamento.external.integration.port.Pedido;
import br.fiap.projeto.pagamento.external.repository.entity.PagamentoEntity;
import br.fiap.projeto.pagamento.external.repository.postgres.SpringPagamentoRepository;
import br.fiap.projeto.pagamento.usecase.EnviaPagamentoAoGatewayPagamentosUseCase;
import br.fiap.projeto.pagamento.usecase.port.repository.IBuscaPagamentoRepositoryAdapterGateway;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class PagamentoIntegrationTest {

    private static final String ENDPOINT_ENVIA_GATEWAY = "/pagamento/gateway/gateway-de-pagamento";
    private static final String ENDPOINT_RETORNO_GATEWAY = "/pagamento/retorno-gateway/atualiza-status";
    private static final String ENDPOINT_BUSCA_PAGAMENTO = "/pagamento/busca";
    private static final String ENDPOINT_LISTA_PAGAMENTO = "/pagamento/busca/todos";
    private static final String ENDPOINT_NOVO_PAGAMENTO = "/pagamento/processa/novo-pagamento";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IPagamentoPedidoIntegration pagamentoIntegration;

    @MockBean
    private IPedidoIntegration pedidoIntegration;

    @MockBean
    private IBuscaPagamentoRepositoryAdapterGateway pagamentoAdapterGateway;

    @BeforeEach
    public void setUp() {

        List<Pedido> listaDePedidos;
        List<Pagamento> listaDePagamentos;

        listaDePedidos = Collections.singletonList(
                new Pedido(
                        setupRequestToExternalGateway().getCodigoPedido(),
                        setupRequestToExternalGateway().getValorTotal()
                )
        );

        listaDePagamentos = Collections.singletonList(
                new Pagamento(UUID.randomUUID(), listaDePedidos.get(0).getCodigo(), StatusPagamento.PENDING, new Date(), listaDePedidos.get(0).getValorTotal())
        );

        Mockito.when(pedidoIntegration.buscaPedidosAPagar()).thenReturn(listaDePedidos);
        Mockito.doNothing().when(pagamentoIntegration).atualizaStatusPagamentoPedido(Mockito.any());

        Mockito.when(pagamentoAdapterGateway.findByCodigo(Mockito.any())).thenReturn(listaDePagamentos.get(0));
        Mockito.when(pagamentoAdapterGateway.findByCodigoPedidoAndStatusPagamento(Mockito.any(), Mockito.any())).thenReturn(listaDePagamentos);
    }

    @Test
    public void deveriaEnviarPagamentoValidoAoGatewayDePagamentos() throws Exception {
        PagamentoAEnviarAoGatewayDTORequest requestDTO = setupRequestToExternalGateway();

        mockMvc.perform(
                        MockMvcRequestBuilders.post(ENDPOINT_ENVIA_GATEWAY)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(extractObjectToJson(requestDTO))
                )
                .andExpect(status().isOk());
    }

    @Test
    public void deveriaEncontrarPagamentoPorCodigo() throws Exception {

        String url = String.format("%s/%s", ENDPOINT_BUSCA_PAGAMENTO, UUID.randomUUID());
        mockMvc.perform(
                MockMvcRequestBuilders.get(url)
        ).andExpect(status().isOk());
    }

    @Test
    public void deveriaListarTodosPagamentos() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders.get(ENDPOINT_LISTA_PAGAMENTO)
        ).andExpect(status().isOk());
    }

    private String extractObjectToJson(PagamentoAEnviarAoGatewayDTORequest requestDTO) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString;
        return jsonString = objectMapper.writeValueAsString(requestDTO);
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
