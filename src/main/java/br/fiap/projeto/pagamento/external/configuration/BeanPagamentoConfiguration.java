package br.fiap.projeto.pagamento.external.configuration;

import br.fiap.projeto.pagamento.adapter.controller.AtualizaStatusPagamentoRestAdapterController;
import br.fiap.projeto.pagamento.adapter.controller.BuscaPagamentoRestAdapterController;
import br.fiap.projeto.pagamento.adapter.controller.EnviaPagamentoAoGatewayRestAdapterController;
import br.fiap.projeto.pagamento.adapter.controller.ProcessaNovoPagamentoRestAdapterController;
import br.fiap.projeto.pagamento.adapter.controller.rest.port.IAtualizaPagamentoRestAdapterController;
import br.fiap.projeto.pagamento.adapter.controller.rest.port.IBuscaPagamentoRestAdapterController;
import br.fiap.projeto.pagamento.adapter.controller.rest.port.IEnviaPagamentoGatewayRestAdapterController;
import br.fiap.projeto.pagamento.adapter.controller.rest.port.IProcessaPagamentoRestAdapterController;
import br.fiap.projeto.pagamento.adapter.gateway.AtualizaStatusPagamentoRepositoryAdapterGateway;
import br.fiap.projeto.pagamento.adapter.gateway.BuscaPagamentoRepositoryAdapterGateway;
import br.fiap.projeto.pagamento.adapter.gateway.PagamentoPedidoIntegrationGateway;
import br.fiap.projeto.pagamento.adapter.gateway.ProcessaNovoPagamentoRepositoryAdapterGateway;
import br.fiap.projeto.pagamento.external.integration.IPagamentoPedidoIntegration;
import br.fiap.projeto.pagamento.external.integration.IPedidoIntegration;
import br.fiap.projeto.pagamento.external.repository.postgres.SpringPagamentoRepository;
import br.fiap.projeto.pagamento.usecase.*;
import br.fiap.projeto.pagamento.usecase.port.repository.IAtualizaStatusPagamentoRepositoryAdapterGateway;
import br.fiap.projeto.pagamento.usecase.port.repository.IBuscaPagamentoRepositoryAdapterGateway;
import br.fiap.projeto.pagamento.usecase.port.repository.IPagamentoPedidoIntegrationGateway;
import br.fiap.projeto.pagamento.usecase.port.repository.IProcessaNovoPagamentoRepositoryAdapterGateway;
import br.fiap.projeto.pagamento.usecase.port.usecase.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanPagamentoConfiguration {

    @Bean
    IBuscaPagamentoRepositoryAdapterGateway pagamentoAdapterGateway(SpringPagamentoRepository springPagamentoRepository){
        return new BuscaPagamentoRepositoryAdapterGateway(springPagamentoRepository);
    }

    @Bean
    IBuscaPagamentoRestAdapterController pagamentoAdapterController(IBuscaPagamentoUseCase buscaPagamentoUseCase){
        return  new BuscaPagamentoRestAdapterController(buscaPagamentoUseCase);
    }

    @Bean
    IBuscaPagamentoUseCase buscaPagamentoUseCase(IBuscaPagamentoRepositoryAdapterGateway pagamentoAdapterGateway){
        return new BuscaPagamentoUseCase(pagamentoAdapterGateway);
    }

    @Bean
    IProcessaNovoPagamentoRepositoryAdapterGateway processaNovoPagamentoAdapterGateway(SpringPagamentoRepository springPagamentoRepository, IBuscaPagamentoUseCase buscaPagamentoUseCase){
        return new ProcessaNovoPagamentoRepositoryAdapterGateway(springPagamentoRepository, buscaPagamentoUseCase);
    }

    @Bean
    IProcessaPagamentoRestAdapterController processaNovoPagamentoAdapterController(IProcessaNovoPagamentoUseCase processaNovoPagamentoUseCase){
        return new ProcessaNovoPagamentoRestAdapterController(processaNovoPagamentoUseCase);
    }

    @Bean
    IProcessaNovoPagamentoUseCase processaNovoPagamentoUseCase(IProcessaNovoPagamentoRepositoryAdapterGateway processaNovoPagamentoAdapterGateway, IBuscaPagamentoUseCase buscaPagamentoUseCase){
        return new ProcessaNovoPagamentoUseCase(processaNovoPagamentoAdapterGateway, buscaPagamentoUseCase);
    }

    @Bean
    IAtualizaStatusPagamentoRepositoryAdapterGateway atualizaStatusPagamentoAdapterGateway(SpringPagamentoRepository springPagamentoRepository){
        return new AtualizaStatusPagamentoRepositoryAdapterGateway(springPagamentoRepository);
    }

    @Bean
    IAtualizaStatusPagamentoUsecase atualizaStatusPagamentoUsecase(IAtualizaStatusPagamentoRepositoryAdapterGateway atualizaStatusPagamentoAdapterGateway,
                                                                   IBuscaPagamentoUseCase buscaPagamentoUseCase, IPagamentoPedidoIntegrationUseCase pagamentoPedidoIntegrationUseCase){
        return new AtualizaStatusPagamentoUseCase(atualizaStatusPagamentoAdapterGateway,
                buscaPagamentoUseCase, pagamentoPedidoIntegrationUseCase);
    }

    @Bean
    IEnviaPagamentoAoGatewayPagamentosUseCase enviaPagamentoAoGatewayPagamentosUseCase(IBuscaPagamentoUseCase buscaPagamentoUseCase,
                                                                                       IAtualizaStatusPagamentoUsecase atualizaStatusPagamentoUsecase){
        return new EnviaPagamentoAoGatewayPagamentosUseCase(buscaPagamentoUseCase, atualizaStatusPagamentoUsecase);
    }

    @Bean
    IEnviaPagamentoGatewayRestAdapterController enviaPagamentoGatewayRestAdapterController(IEnviaPagamentoAoGatewayPagamentosUseCase enviaPagamentoAoGatewayPagamentosUseCase){
        return new EnviaPagamentoAoGatewayRestAdapterController(enviaPagamentoAoGatewayPagamentosUseCase);
    }

    @Bean
    IAtualizaPagamentoRestAdapterController atualizaPagamentoRestAdapterController(IAtualizaStatusPagamentoUsecase atualizaStatusPagamentoUsecase){
        return new AtualizaStatusPagamentoRestAdapterController(atualizaStatusPagamentoUsecase);
    }
    @Bean
    IPagamentoPedidoIntegrationGateway pagamentoPedidoIntegrationGateway(IPedidoIntegration pedidoIntegration, IPagamentoPedidoIntegration pagamentoPedidoIntegration){
        return new PagamentoPedidoIntegrationGateway(pedidoIntegration, pagamentoPedidoIntegration);
    }

    @Bean
    IPagamentoPedidoIntegrationUseCase pagamentoPedidoIntegrationUseCase(IPagamentoPedidoIntegrationGateway pagamentoPedidoIntegrationGateway, IBuscaPagamentoUseCase buscaPagamentoUseCase){
        return new PagamentoPedidoIntegrationUseCase(pagamentoPedidoIntegrationGateway, buscaPagamentoUseCase);
    }
}