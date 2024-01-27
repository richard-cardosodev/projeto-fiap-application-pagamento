package br.fiap.projeto.pagamento;

import br.fiap.projeto.pagamento.adapter.controller.rest.request.PagamentoAEnviarAoGatewayDTORequest;
import br.fiap.projeto.pagamento.entity.enums.StatusPagamento;
import br.fiap.projeto.pagamento.usecase.EnviaPagamentoAoGatewayPagamentosUseCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Date;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class PagamentoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
   // private static final WireMockServer wireMockServer = new WireMockServer(WireMockConfiguration.options().port(9080));

    @InjectMocks
    private EnviaPagamentoAoGatewayPagamentosUseCase enviaPagamentoAoGatewayPagamentosUseCase;

    private final String ENDPOINT_ENVIA_GATEWAY = "http://localhost:8080/pagamento/pagamento/gateway/gateway-de-pagamento";
    private final String ENDPOINT_RETORNO_GATEWAY = "http://localhost:8080/pagamento/pagamento/retorno-gateway/atualiza-status";
    private final String ENDPOINT_BUSCA_PAGAMENTO = "http://localhost:8080/pagamento/pagamento/busca";
    private final String ENDPOINT_NOVO_PAGAMENTO = "http://localhost:8080/pagamento/pagamento/processa/novo-pagamento";


    private String jsonString;

    @Test
    public void deveriaEnviarPagamentoValidoAoGatewayDePagamentos() throws Exception {
        PagamentoAEnviarAoGatewayDTORequest requestDTO = setupRequestToExternalGateway();

        mockMvc.perform(
                        MockMvcRequestBuilders.post(ENDPOINT_ENVIA_GATEWAY)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(extractObjectToJson(requestDTO))
                )
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("codigoPedido")));
    }


    private String extractObjectToJson(PagamentoAEnviarAoGatewayDTORequest requestDTO) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
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
