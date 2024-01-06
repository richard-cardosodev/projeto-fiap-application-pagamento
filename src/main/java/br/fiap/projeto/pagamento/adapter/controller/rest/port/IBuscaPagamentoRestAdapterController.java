package br.fiap.projeto.pagamento.adapter.controller.rest.port;

import br.fiap.projeto.pagamento.adapter.controller.rest.response.PagamentoDTOResponse;
import br.fiap.projeto.pagamento.entity.enums.StatusPagamento;

import java.util.List;
import java.util.UUID;

public interface IBuscaPagamentoRestAdapterController {

    List<PagamentoDTOResponse> findAll();
    PagamentoDTOResponse findByCodigo(UUID codigo);

    List<PagamentoDTOResponse> findByStatusPagamento(StatusPagamento status);

    PagamentoDTOResponse findByCodigoPedido(String codigoPedido);
}
