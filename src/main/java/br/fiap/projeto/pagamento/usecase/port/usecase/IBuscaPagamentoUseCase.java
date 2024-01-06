package br.fiap.projeto.pagamento.usecase.port.usecase;

import br.fiap.projeto.pagamento.entity.Pagamento;
import br.fiap.projeto.pagamento.entity.enums.StatusPagamento;

import java.util.List;
import java.util.UUID;

public interface IBuscaPagamentoUseCase {

    List<Pagamento> findAll();

    Pagamento findByCodigo(UUID codigo);

    List<Pagamento> findByStatusPagamento(StatusPagamento status);

    List<Pagamento> findByCodigoPedido(String codigoPedido);

    Pagamento findByCodigoPedidoNotRejected(String codigoPedido);

    Pagamento findByCodigoPedidoRejected(String codigoPedido);

    Pagamento findByCodigoPedidoPending(String codigoPedido);

    Pagamento findByCodigoPedidoInProcess(String codigoPedido);
}
