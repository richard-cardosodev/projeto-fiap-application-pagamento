package br.fiap.projeto.pagamento.adapter.controller.rest.port;

import br.fiap.projeto.pagamento.adapter.controller.rest.request.PedidoAPagarDTORequest;
import br.fiap.projeto.pagamento.adapter.controller.rest.response.PagamentoNovoDTOResponse;


public interface IProcessaPagamentoRestAdapterController {

     PagamentoNovoDTOResponse criaNovoPagamento(PedidoAPagarDTORequest pedidoAPagarDTORequest);

}
