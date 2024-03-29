package br.fiap.projeto.pagamento.adapter.controller;

import br.fiap.projeto.pagamento.adapter.controller.rest.port.IEnviaPagamentoGatewayRestAdapterController;
import br.fiap.projeto.pagamento.adapter.controller.rest.request.PagamentoAEnviarAoGatewayDTORequest;
import br.fiap.projeto.pagamento.usecase.port.usecase.IEnviaPagamentoAoGatewayPagamentosUseCase;

import java.util.logging.Logger;

public class EnviaPagamentoAoGatewayRestAdapterController implements IEnviaPagamentoGatewayRestAdapterController {

    private final Logger logger = Logger.getLogger(getClass().getName());
    private final IEnviaPagamentoAoGatewayPagamentosUseCase enviaPagamentoAoGatewayPagamentosUseCase;

    public EnviaPagamentoAoGatewayRestAdapterController(IEnviaPagamentoAoGatewayPagamentosUseCase enviaPagamentoAoGatewayPagamentosUseCase) {
        this.enviaPagamentoAoGatewayPagamentosUseCase = enviaPagamentoAoGatewayPagamentosUseCase;
    }

    @Override
    public PagamentoAEnviarAoGatewayDTORequest preparaParaEnviarPagamentoAoGateway(PagamentoAEnviarAoGatewayDTORequest pagamentoDTORequest) {
        return new PagamentoAEnviarAoGatewayDTORequest(
                enviaPagamentoAoGatewayPagamentosUseCase.preparaParaEnviarPagamentoAoGateway(
                        pagamentoDTORequest.getCodigoPedido()));
    }

    @Override
    public void enviaParaGatewayDePagamento(PagamentoAEnviarAoGatewayDTORequest pagamentoDTORequest) {
        logger.info("RestAdapterController: Enviando request de pagamento ao sistema externo...");
        enviaPagamentoAoGatewayPagamentosUseCase
                .enviaRequestAoSistemaExternoPagamentos(
                        pagamentoDTORequest.getCodigoPedido(), pagamentoDTORequest.getStatusPagamento()
                );
    }

}
