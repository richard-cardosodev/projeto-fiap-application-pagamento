package br.fiap.projeto.pagamento.adapter.controller.rest.port;

import br.fiap.projeto.pagamento.adapter.controller.rest.request.PagamentoDTORequest;

public interface IAtualizaPagamentoRestAdapterController {

    void atualizaStatusPagamento(PagamentoDTORequest pagamentoDTORequest);

}
