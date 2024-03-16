package br.fiap.projeto.pagamento.adapter.gateway;

import br.fiap.projeto.pagamento.entity.Pagamento;
import br.fiap.projeto.pagamento.usecase.exceptions.JsonProcessingException;
import br.fiap.projeto.pagamento.usecase.port.IJsonConverter;
import br.fiap.projeto.pagamento.usecase.port.messaging.IPagamentoConfirmadoQueueOUT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;

public class PagamentoConfirmadoQueueOUTAdapterGateway implements IPagamentoConfirmadoQueueOUT {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private RabbitTemplate rabbitTemplate;

    private IJsonConverter jsonConverter;

    @Value("${pagamento.confirmado.queue}")
    private String pagamentosConfirmados;

    public PagamentoConfirmadoQueueOUTAdapterGateway(RabbitTemplate rabbitTemplate, IJsonConverter jsonConverter) {
        this.rabbitTemplate = rabbitTemplate;
        this.jsonConverter = jsonConverter;
    }

    @Override
    public void publish(Pagamento pagamento) throws JsonProcessingException {
        this.rabbitTemplate.convertAndSend(pagamentosConfirmados, jsonConverter.convertObjectToJsonString(pagamento));
        logger.info("Publicado na fila de pagamentos confirmados!");
    }
}
