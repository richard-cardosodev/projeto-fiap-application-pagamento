package br.fiap.projeto.pagamento.usecase;

import br.fiap.projeto.pagamento.entity.Pagamento;
import br.fiap.projeto.pagamento.entity.enums.StatusPagamento;
import br.fiap.projeto.pagamento.usecase.exceptions.ResourceNotFoundException;
import br.fiap.projeto.pagamento.usecase.exceptions.mensagens.MensagemDeErro;
import br.fiap.projeto.pagamento.usecase.port.repository.IBuscaPagamentoRepositoryAdapterGateway;
import br.fiap.projeto.pagamento.usecase.port.usecase.IBuscaPagamentoUseCase;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public class BuscaPagamentoUseCase implements IBuscaPagamentoUseCase {

    private final IBuscaPagamentoRepositoryAdapterGateway pagamentoAdapterGateway;
    public BuscaPagamentoUseCase(IBuscaPagamentoRepositoryAdapterGateway pagamentoAdapterGateway) {
        this.pagamentoAdapterGateway = pagamentoAdapterGateway;
    }

    @Override
    public List<Pagamento> findAll() {
        return pagamentoAdapterGateway.findAll();
    }

    @Override
    public Pagamento findByCodigo(UUID codigo) {
        Optional<Pagamento> possivelPagamento = Optional.ofNullable(pagamentoAdapterGateway.findByCodigo(codigo));

        if (possivelPagamento.isPresent()) {
            return possivelPagamento.get();
        } else {
            throw new ResourceNotFoundException(MensagemDeErro.PAGAMENTO_NAO_ENCONTRADO.getMessage());
        }
    }

    @Override
    public List<Pagamento> findByStatusPagamento(StatusPagamento status) {
        return pagamentoAdapterGateway.findByStatusPagamento(status);
    }

    @Override
    public List<Pagamento> findByCodigoPedido(String codigoPedido) {
        Optional<List<Pagamento>> possivelPagamento = Optional.ofNullable(pagamentoAdapterGateway.findByCodigoPedido(codigoPedido));

        if (possivelPagamento.isPresent()) {
            return possivelPagamento.get();
        } else {
            throw new ResourceNotFoundException(MensagemDeErro.PEDIDO_PAGAMENTO_NAO_ENCONTRADO.getMessage());
        }
    }

    @Override
    public Pagamento findByCodigoPedidoNotRejected(String codigoPedido) {
        try{
            Optional<List<Pagamento>> possivelPagamento = Optional.ofNullable(pagamentoAdapterGateway.findByCodigoPedidoAndStatusPagamentoNotRejected(codigoPedido, StatusPagamento.REJECTED));
            return possivelPagamento.get().stream().findFirst().get();
        }catch(NoSuchElementException elementException){
            throw new ResourceNotFoundException(MensagemDeErro.PEDIDO_PAGAMENTO_NAO_ENCONTRADO.getMessage());
        }
    }

    @Override
    public Pagamento findByCodigoPedidoRejected(String codigoPedido) {
        Optional<List<Pagamento>> possivelPagamento = Optional.ofNullable(pagamentoAdapterGateway.findByCodigoPedidoAndStatusPagamento(codigoPedido, StatusPagamento.REJECTED));
        return possivelPagamento
                .flatMap(pagamentos -> pagamentos.stream().findFirst())
                .orElseThrow(() -> new ResourceNotFoundException(MensagemDeErro.PEDIDO_PAGAMENTO_NAO_ENCONTRADO.getMessage()));
    }
    @Override
    public Pagamento findByCodigoPedidoPending(String codigoPedido) {
        Optional<List<Pagamento>> possivelPagamento = Optional.ofNullable(pagamentoAdapterGateway.findByCodigoPedidoAndStatusPagamento(codigoPedido, StatusPagamento.PENDING));
        return possivelPagamento
                .flatMap(pagamentos -> pagamentos.stream().findFirst())
                .orElseThrow(() -> new ResourceNotFoundException(MensagemDeErro.PEDIDO_PAGAMENTO_NAO_ENCONTRADO.getMessage()));
    }
    @Override
    public Pagamento findByCodigoPedidoInProcess(String codigoPedido) {
        Optional<List<Pagamento>> possivelPagamento = Optional.ofNullable(pagamentoAdapterGateway.findByCodigoPedidoAndStatusPagamento(codigoPedido, StatusPagamento.IN_PROCESS));

        if (possivelPagamento.isPresent()) {
            List<Pagamento> pagamentos = possivelPagamento.get();
            if (!pagamentos.isEmpty()) {
                return pagamentos.get(0);
            }
        }

        throw new ResourceNotFoundException(MensagemDeErro.PEDIDO_PAGAMENTO_NAO_ENCONTRADO.getMessage());
    }
}
