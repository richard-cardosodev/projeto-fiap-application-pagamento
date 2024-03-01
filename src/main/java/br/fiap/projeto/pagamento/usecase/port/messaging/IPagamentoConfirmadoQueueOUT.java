package br.fiap.projeto.pagamento.usecase.port.messaging;

import br.fiap.projeto.pagamento.entity.Pagamento;
import br.fiap.projeto.pagamento.usecase.exceptions.JsonProcessingException;

public interface IPagamentoConfirmadoQueueOUT {
    void publish(Pagamento pagamento) throws JsonProcessingException;
}
