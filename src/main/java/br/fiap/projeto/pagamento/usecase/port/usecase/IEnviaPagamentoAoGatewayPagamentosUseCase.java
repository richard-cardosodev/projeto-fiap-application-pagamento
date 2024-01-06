package br.fiap.projeto.pagamento.usecase.port.usecase;

import br.fiap.projeto.pagamento.entity.Pagamento;
import br.fiap.projeto.pagamento.entity.enums.StatusPagamento;

public interface IEnviaPagamentoAoGatewayPagamentosUseCase {

    void enviaRequestAoSistemaExternoPagamentos(String codigoPedido, StatusPagamento status);

    Pagamento preparaParaEnviarPagamentoAoGateway(String codigoPedido);
}
