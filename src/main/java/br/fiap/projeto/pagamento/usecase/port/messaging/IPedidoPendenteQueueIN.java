package br.fiap.projeto.pagamento.usecase.port.messaging;

import br.fiap.projeto.pagamento.usecase.exceptions.JsonProcessingException;

public interface IPedidoPendenteQueueIN {
    void receive(String message) throws JsonProcessingException;
}
