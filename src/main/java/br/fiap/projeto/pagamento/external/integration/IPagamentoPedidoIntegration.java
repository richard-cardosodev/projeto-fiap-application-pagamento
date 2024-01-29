package br.fiap.projeto.pagamento.external.integration;

import br.fiap.projeto.pagamento.entity.integration.PagamentoPedidoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value="pagamentoPedidoIntegration", url = "http://${pedido.host}/pedidos")
public interface IPagamentoPedidoIntegration {

    @PutMapping(value="/recebe-retorno-pagamento")
    void atualizaStatusPagamentoPedido(@RequestBody PagamentoPedidoResponse pagamentoPedidoResponse);

}

