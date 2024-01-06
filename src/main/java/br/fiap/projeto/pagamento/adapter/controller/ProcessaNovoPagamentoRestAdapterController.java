package br.fiap.projeto.pagamento.adapter.controller;

import br.fiap.projeto.pagamento.adapter.controller.rest.port.IProcessaPagamentoRestAdapterController;
import br.fiap.projeto.pagamento.adapter.controller.rest.request.PedidoAPagarDTORequest;
import br.fiap.projeto.pagamento.adapter.controller.rest.response.PagamentoNovoDTOResponse;
import br.fiap.projeto.pagamento.usecase.port.usecase.IProcessaNovoPagamentoUseCase;

public class ProcessaNovoPagamentoRestAdapterController implements IProcessaPagamentoRestAdapterController {

    private final IProcessaNovoPagamentoUseCase processaNovoPagamentoUseCase;

    public ProcessaNovoPagamentoRestAdapterController(IProcessaNovoPagamentoUseCase processaNovoPagamentoUseCase) {
        this.processaNovoPagamentoUseCase = processaNovoPagamentoUseCase;
    }

    @Override
    public PagamentoNovoDTOResponse criaNovoPagamento(PedidoAPagarDTORequest pedidoAPagarDTORequest) {
        return new PagamentoNovoDTOResponse(processaNovoPagamentoUseCase
                                    .criaNovoPagamento(
                                            pedidoAPagarDTORequest.conversorDePedidoAPagarDTOParaPagamento()));
    }

}
