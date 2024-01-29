package br.fiap.projeto.pagamento.usecase;

import br.fiap.projeto.pagamento.entity.Pagamento;
import br.fiap.projeto.pagamento.entity.enums.StatusPagamento;
import br.fiap.projeto.pagamento.entity.integration.PagamentoPedidoResponse;
import br.fiap.projeto.pagamento.external.integration.exceptions.InvalidOperationIntegrationException;
import br.fiap.projeto.pagamento.external.integration.exceptions.PagamentoPedidoIntegrationException;
import br.fiap.projeto.pagamento.usecase.exceptions.mensagens.MensagemDeErro;
import br.fiap.projeto.pagamento.usecase.port.repository.IPagamentoPedidoIntegrationGateway;
import br.fiap.projeto.pagamento.usecase.port.usecase.IBuscaPagamentoUseCase;
import br.fiap.projeto.pagamento.usecase.port.usecase.IPagamentoPedidoIntegrationUseCase;
import feign.FeignException;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class PagamentoPedidoIntegrationUseCase implements IPagamentoPedidoIntegrationUseCase {

    private final Logger logger = Logger.getLogger(getClass().getName());
    private final IPagamentoPedidoIntegrationGateway pagamentoPedidoIntegrationGateway;

    private final IBuscaPagamentoUseCase buscaPagamentoUseCase;

    private ScheduledExecutorService scheduler = null;

    public PagamentoPedidoIntegrationUseCase(IPagamentoPedidoIntegrationGateway pagamentoPedidoIntegrationGateway, IBuscaPagamentoUseCase buscaPagamentoUseCase) {
        this.pagamentoPedidoIntegrationGateway = pagamentoPedidoIntegrationGateway;
        this.buscaPagamentoUseCase  = buscaPagamentoUseCase;
    }

   @Override
    public void atualizarPagamentoPedido(Pagamento pagamento) {
        PagamentoPedidoResponse response = new PagamentoPedidoResponse(pagamento.getCodigoPedido(), pagamento.getStatus().name(), LocalDateTime.now());
        pagamentoPedidoIntegrationGateway.atualizaStatusPagamentoPedido(response);
    }

    @Override
    public void scheduleAtualizaPagamentoPedido(String codigoPedido) {
        Pagamento pagamento = getPagamento(codigoPedido);
        this.scheduler = Executors.newScheduledThreadPool(1);
        scheduler.schedule(() -> {
            try{
                atualizarPagamentoPedido(pagamento);
            }catch(FeignException fe){
                logger.info("Não foi possível atualizar o status do Pedido. Integração entre serviços falhou.");
                throw new PagamentoPedidoIntegrationException(MensagemDeErro.ERRO_INTEGRACAO.getMessage());
            }catch(Exception e){
                logger.info("Não foi possível atualizar o status do Pedido. Operação inválida durante a integração.");
                throw new InvalidOperationIntegrationException(MensagemDeErro.ENVIO_ATUALIZACAO_STATUS_INTEGRACAO_FALHA.getMessage());
            } finally {
                shutDownScheduler();
            }
        }, 3, TimeUnit.SECONDS);
    }

    private Pagamento getPagamento(String codigoPedido) {
        Optional<Pagamento> optionalPagamento = buscaPagamentoUseCase.findByCodigoPedido(codigoPedido)
                .stream()
                .filter(p -> p.getStatus().equals(StatusPagamento.APPROVED) || p.getStatus().equals(StatusPagamento.CANCELLED))
                .findFirst();
        if (optionalPagamento.isPresent()) {
            return optionalPagamento.get();
        } else {
            throw new NoSuchElementException(MensagemDeErro.PAGAMENTO_NAO_ENCONTRADO.getMessage());
        }
    }

    @Override
    public void shutDownScheduler() {
        scheduler.shutdown();
    }
}
