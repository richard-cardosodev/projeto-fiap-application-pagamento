package br.fiap.projeto.pagamento.usecase;

import br.fiap.projeto.pagamento.entity.Pagamento;
import br.fiap.projeto.pagamento.entity.enums.StatusPagamento;
import br.fiap.projeto.pagamento.usecase.exceptions.JsonProcessingException;
import br.fiap.projeto.pagamento.usecase.exceptions.UnprocessablePaymentException;
import br.fiap.projeto.pagamento.usecase.exceptions.mensagens.MensagemDeErro;
import br.fiap.projeto.pagamento.usecase.port.messaging.IPagamentoCanceladoQueueOUT;
import br.fiap.projeto.pagamento.usecase.port.messaging.IPagamentoConfirmadoQueueOUT;
import br.fiap.projeto.pagamento.usecase.port.repository.IAtualizaStatusPagamentoRepositoryAdapterGateway;
import br.fiap.projeto.pagamento.usecase.port.usecase.IAtualizaStatusPagamentoUsecase;
import br.fiap.projeto.pagamento.usecase.port.usecase.IBuscaPagamentoUseCase;

public class AtualizaStatusPagamentoUseCase implements IAtualizaStatusPagamentoUsecase {

    private final IAtualizaStatusPagamentoRepositoryAdapterGateway atualizaStatusPagamentoAdapterGateway;
    private final IBuscaPagamentoUseCase buscaPagamentoUseCase;
    private final IPagamentoConfirmadoQueueOUT pagamentoConfirmadoQueueOUT;
    private final IPagamentoCanceladoQueueOUT pagamentoCanceladoQueueOUT;

    public AtualizaStatusPagamentoUseCase(IAtualizaStatusPagamentoRepositoryAdapterGateway atualizaStatusPagamentoAdapterGateway,
                                          IBuscaPagamentoUseCase buscaPagamentoUseCase,
                                          IPagamentoConfirmadoQueueOUT pagamentoConfirmadoQueueOUT,
                                          IPagamentoCanceladoQueueOUT pagamentoCanceladoQueueOUT) {
        this.atualizaStatusPagamentoAdapterGateway = atualizaStatusPagamentoAdapterGateway;
        this.buscaPagamentoUseCase = buscaPagamentoUseCase;
        this.pagamentoConfirmadoQueueOUT = pagamentoConfirmadoQueueOUT;
        this.pagamentoCanceladoQueueOUT = pagamentoCanceladoQueueOUT;
    }

    /**
     * Utilizado pelo retorno do Gateway de Pagamentos para atualizar o Pagamento
     * com a resposta do sistema bancário externo. Compara o status passado na request
     * (novo status) com o status existente do pagamento, as possíveis transições de estado são:
     * <li>IN_PROCESS -> APPROVED ou CANCELLED</li>
     * <li>REJECTED -> CANCELLED</li>
     * <li>PENDING ->  IN_PROCESS somente via atualização de status pelo método de envio ao gateway, não deve ser atualizado aqui</li>
     *
     * @param codigoPedido
     * @param novoStatusPagamento
     */
    @Override
    public void atualizaStatusPagamento(String codigoPedido, StatusPagamento novoStatusPagamento) throws JsonProcessingException {
        Pagamento pagamento;
        switch (novoStatusPagamento){
            case CANCELLED:
                pagamento = buscaPagamentoUseCase.findByCodigoPedidoRejected(codigoPedido);
                pagamento.cancelaPagamento(pagamento);
                break;
            case REJECTED:
                pagamento = buscaPagamentoUseCase.findByCodigoPedidoInProcess(codigoPedido);
                pagamento.rejeitaPagamento(pagamento);
                break;
            case APPROVED:
                pagamento = buscaPagamentoUseCase.findByCodigoPedidoInProcess(codigoPedido);
                pagamento.aprovaPagamento(pagamento);
                break;
            case IN_PROCESS:
                throw new UnprocessablePaymentException(MensagemDeErro.PAGAMENTO_DEVE_SER_ENVIADO_AO_GATEWAY.getMessage());
            case PENDING:
                throw new UnprocessablePaymentException(MensagemDeErro.PAGAMENTO_PENDENTE.getMessage());
            default:
                return;
        }
        salvaStatus(pagamento);

        if(novoStatusPagamento.equals(StatusPagamento.APPROVED)) {
            this.pagamentoConfirmadoQueueOUT.publish(pagamento);
        }
        if(novoStatusPagamento.equals(StatusPagamento.CANCELLED)) {
            this.pagamentoCanceladoQueueOUT.publish(pagamento);
        }
    }

    /**
     * Utilizado para atualizar o status do pagamento ao ser enviado para o Gateway de Pagamentos.
     * A única transição entre estados possíveis é:
     * <li>PENDING -> IN_PROCESS</li>
     * Atualização de pendente para em processamento deverá acontecer somente ao
     * enviar par ao Gateway de Pagamentos.
     * @param codigoPedido
     * @param statusPagamento
     */
    @Override
    public void atualizaStatusPagamentoGateway(String codigoPedido, StatusPagamento statusPagamento) {
        Pagamento pagamento = buscaPagamentoUseCase.findByCodigoPedidoPending(codigoPedido);
        if(!pagamento.podeSerProcessado(pagamento.getStatus(), statusPagamento)){
            throw new UnprocessablePaymentException(MensagemDeErro.STATUS_INVALIDO.getMessage());
        }
        pagamento.colocaEmProcessamento(pagamento);
        salvaStatus(pagamento);
    }
    private void salvaStatus(Pagamento pagamento) {
        atualizaStatusPagamentoAdapterGateway.atualizaStatusPagamento(pagamento);
    }
}