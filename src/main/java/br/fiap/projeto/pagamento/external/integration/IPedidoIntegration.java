package br.fiap.projeto.pagamento.external.integration;

import br.fiap.projeto.pagamento.external.integration.port.Pedido;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@FeignClient(value="pedidoIntegration", url = "http://${pedido.host}/pedidos")
public interface IPedidoIntegration {

    @GetMapping(value = "/busca-recebidos")
    List<Pedido> buscaPedidosAPagar();
}
