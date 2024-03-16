package br.fiap.projeto.pagamento.external.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Value("${pedido.pendente.queue}")
    private String pedidosPendentesQueue;
    @Value("${pagamento.confirmado.queue}")
    private String pagamentosConfirmadosQueue;
    @Value("${pagamento.cancelado.queue}")
    private String pagamentosCanceladosQueue;

    @Bean
    public Queue pedidosPendentes() {
        return new Queue(pedidosPendentesQueue, true, false, false);
    }
    @Bean
    public Queue pagamentosConfirmados() {
        return new Queue(pagamentosConfirmadosQueue, true, false, false);
    }
    @Bean
    public Queue pagamentosCancelados() {
        return new Queue(pagamentosCanceladosQueue, true, false, false);
    }
}

