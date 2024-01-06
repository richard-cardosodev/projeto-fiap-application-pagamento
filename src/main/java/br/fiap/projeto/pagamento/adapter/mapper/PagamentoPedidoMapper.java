package br.fiap.projeto.pagamento.adapter.mapper;

import br.fiap.projeto.pagamento.entity.integration.PagamentoPedido;
import br.fiap.projeto.pagamento.external.integration.port.Pedido;

public class PagamentoPedidoMapper {
    public static PagamentoPedido toPagamentoPedido(Pedido pedido) {
        return new PagamentoPedido(pedido.getCodigo(),pedido.getValorTotal());
    }
}