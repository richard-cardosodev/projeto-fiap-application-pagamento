package br.fiap.projeto.pagamento.usecase;

import br.fiap.projeto.pagamento.entity.Pagamento;
import br.fiap.projeto.pagamento.entity.enums.StatusPagamento;
import br.fiap.projeto.pagamento.usecase.exceptions.ResourceNotFoundException;
import br.fiap.projeto.pagamento.usecase.exceptions.UnprocessablePaymentException;
import br.fiap.projeto.pagamento.usecase.exceptions.mensagens.MensagemDeErro;
import br.fiap.projeto.pagamento.usecase.port.usecase.IAtualizaStatusPagamentoUsecase;
import br.fiap.projeto.pagamento.usecase.port.usecase.IBuscaPagamentoUseCase;
import br.fiap.projeto.pagamento.usecase.port.usecase.IEnviaPagamentoAoGatewayPagamentosUseCase;

import java.util.NoSuchElementException;
import java.util.logging.Logger;

public class EnviaPagamentoAoGatewayPagamentosUseCase implements IEnviaPagamentoAoGatewayPagamentosUseCase {

    private final Logger logger = Logger.getLogger(getClass().getName());
    private final IBuscaPagamentoUseCase buscaPagamentoUseCase;

    private final IAtualizaStatusPagamentoUsecase atualizaStatusPagamentoUsecase;

    public EnviaPagamentoAoGatewayPagamentosUseCase(IBuscaPagamentoUseCase buscaPagamentoUseCase,
                                                    IAtualizaStatusPagamentoUsecase atualizaStatusPagamentoUsecase) {
        this.buscaPagamentoUseCase = buscaPagamentoUseCase;
        this.atualizaStatusPagamentoUsecase = atualizaStatusPagamentoUsecase;
    }

   @Override
    public void enviaRequestAoSistemaExternoPagamentos(String codigoPedido, StatusPagamento status) {
       logger.info("Enviando pagamento ao Sistema Externo de Pagamentos: MercadoPago");
       logger.info("Pagamento agora será processado pelo Gateway de Pagamento.");
   }

    @Override
    public Pagamento preparaParaEnviarPagamentoAoGateway(String codigoPedido) {
        this.verificaPagamentoAntesDeEnviarAoGateway(codigoPedido);
        this.validaStatusAtualDoPagamentoAntesDeEnviarAoGateway(codigoPedido);
        return this.atualizaStatusNovoAoEnviarPagamentoAoGateway(codigoPedido);
    }

    private Pagamento getPagamento(String codigoPedido) {
        try {
            return buscaPagamentoUseCase.findByCodigoPedidoPending(codigoPedido);
        }catch(NoSuchElementException elementException){
            throw new ResourceNotFoundException(elementException.getMessage());
        }
    }

    private void verificaPagamentoAntesDeEnviarAoGateway(String codigoPedido) {
        if(getPagamento(codigoPedido).getCodigo() == (null)){
            throw new ResourceNotFoundException(MensagemDeErro.PAGAMENTO_NAO_ENCONTRADO.getMessage());
        }
    }

    private void validaStatusAtualDoPagamentoAntesDeEnviarAoGateway(String codigoPedido) {
        Pagamento pagamentoStatusAtual = getPagamento(codigoPedido);
        if(!pagamentoStatusAtual.getStatus().equals(StatusPagamento.PENDING)){
            throw new UnprocessablePaymentException(MensagemDeErro.STATUS_INVALIDO_ENVIO_GATEWAY.getMessage());
        }
    }

    private Pagamento atualizaStatusNovoAoEnviarPagamentoAoGateway(String codigoPedido) {
        Pagamento pagamentoAnalisado = getPagamento(codigoPedido);
        atualizaStatusPagamentoUsecase.atualizaStatusPagamentoGateway(codigoPedido, StatusPagamento.IN_PROCESS);
        return pagamentoAnalisado;
    }

}
