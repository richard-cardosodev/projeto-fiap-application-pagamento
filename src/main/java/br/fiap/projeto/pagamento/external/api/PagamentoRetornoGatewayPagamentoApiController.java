package br.fiap.projeto.pagamento.external.api;

import br.fiap.projeto.pagamento.adapter.controller.rest.port.IAtualizaPagamentoRestAdapterController;
import br.fiap.projeto.pagamento.adapter.controller.rest.request.PagamentoDTORequest;
import br.fiap.projeto.pagamento.adapter.controller.rest.request.PagamentoStatusDTORequest;
import br.fiap.projeto.pagamento.usecase.exceptions.JsonProcessingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pagamento/retorno-gateway")
@Api(tags = {"Pagamento - Integração"})
public class PagamentoRetornoGatewayPagamentoApiController {

    private final IAtualizaPagamentoRestAdapterController atualizaPagamentoRestAdapterController;

    @Autowired
    public PagamentoRetornoGatewayPagamentoApiController(IAtualizaPagamentoRestAdapterController atualizaPagamentoRestAdapterController) {
        this.atualizaPagamentoRestAdapterController = atualizaPagamentoRestAdapterController;
    }

    @Transactional
    @PatchMapping(value="/atualiza-status")
    @ApiOperation(value="Atualiza Status dos Pagamentos", notes="Esse endpoint atualiza os status dos pagamentos, simulando o retorno que virá após envio ao Gateway de Pagamentos.")
    public ResponseEntity<Void> atualizaStatusComRespostaDoGatewayPagamento(@RequestBody PagamentoStatusDTORequest pagamentoStatusDTORequest) throws JsonProcessingException {
        atualizaPagamentoRestAdapterController.atualizaStatusPagamento(new PagamentoDTORequest(pagamentoStatusDTORequest));
        return ResponseEntity.ok().build() ;
    }
}