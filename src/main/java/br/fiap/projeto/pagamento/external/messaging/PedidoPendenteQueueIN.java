package br.fiap.projeto.pagamento.external.messaging;

import br.fiap.projeto.pagamento.adapter.controller.rest.port.IProcessaPagamentoRestAdapterController;
import br.fiap.projeto.pagamento.adapter.controller.rest.request.PedidoAPagarDTORequest;
import br.fiap.projeto.pagamento.usecase.exceptions.JsonProcessingException;
import br.fiap.projeto.pagamento.usecase.port.IJsonConverter;
import br.fiap.projeto.pagamento.usecase.port.messaging.IPedidoPendenteQueueIN;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class PedidoPendenteQueueIN implements IPedidoPendenteQueueIN {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final IJsonConverter jsonConverter;
    private final IProcessaPagamentoRestAdapterController processaPagamentoRestAdapterController;

    public PedidoPendenteQueueIN(IJsonConverter jsonConverter, IProcessaPagamentoRestAdapterController processaPagamentoRestAdapterController) {
        this.jsonConverter = jsonConverter;
        this.processaPagamentoRestAdapterController = processaPagamentoRestAdapterController;
    }

    @Transactional
    @RabbitListener(queues = {"${pedido.pendente.queue}"})
    @Override
    public void receive(String message) throws JsonProcessingException {
        Map<String, Object> messageMap = jsonConverter.stringJsonToMapStringObject(message);
        PedidoAPagarDTORequest pedidoAPagarDTORequest = new PedidoAPagarDTORequest((String) messageMap.get("codigo"), (Double) messageMap.get("valorTotal"));
        processaPagamentoRestAdapterController.criaNovoPagamento(pedidoAPagarDTORequest);
        logger.info("Efetuada a leitura da fila de pedidos pendentes!");
    }
}
