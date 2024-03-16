package br.fiap.projeto.pagamento.adapter.controller.rest.port;

import br.fiap.projeto.pagamento.adapter.controller.rest.request.PagamentoDTORequest;
import br.fiap.projeto.pagamento.usecase.exceptions.JsonProcessingException;

public interface IAtualizaPagamentoRestAdapterController {

    void atualizaStatusPagamento(PagamentoDTORequest pagamentoDTORequest) throws JsonProcessingException;

}
