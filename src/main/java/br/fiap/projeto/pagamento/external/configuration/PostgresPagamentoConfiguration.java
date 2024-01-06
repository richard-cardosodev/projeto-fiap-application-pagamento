package br.fiap.projeto.pagamento.external.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "br.fiap.projeto.pagamento.external.repository")
@EntityScan("br.fiap.projeto.pagamento.external.repository.entity")
public class PostgresPagamentoConfiguration {

}
