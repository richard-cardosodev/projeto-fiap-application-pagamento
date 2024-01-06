package br.fiap.projeto.pagamento.adapter.controller;

import br.fiap.projeto.pagamento.adapter.controller.rest.port.IBuscaPagamentoRestAdapterController;
import br.fiap.projeto.pagamento.adapter.controller.rest.response.PagamentoDTOResponse;
import br.fiap.projeto.pagamento.entity.Pagamento;
import br.fiap.projeto.pagamento.entity.enums.StatusPagamento;
import br.fiap.projeto.pagamento.usecase.port.usecase.IBuscaPagamentoUseCase;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class BuscaPagamentoRestAdapterController implements IBuscaPagamentoRestAdapterController {

    private final IBuscaPagamentoUseCase buscaPagamentoUseCase;

    public BuscaPagamentoRestAdapterController(IBuscaPagamentoUseCase buscaPagamentoUseCase) {
        this.buscaPagamentoUseCase = buscaPagamentoUseCase;
    }

    @Override
    public List<PagamentoDTOResponse> findAll() {
        return convertList(buscaPagamentoUseCase.findAll());
    }

    @Override
    public PagamentoDTOResponse findByCodigo(UUID codigo) {
        return new PagamentoDTOResponse(buscaPagamentoUseCase.findByCodigo(codigo));
    }

    @Override
    public List<PagamentoDTOResponse> findByStatusPagamento(StatusPagamento status) {
        return convertList(buscaPagamentoUseCase.findByStatusPagamento(status));
    }

    @Override
    public PagamentoDTOResponse findByCodigoPedido(String codigoPedido) {
        return new PagamentoDTOResponse(buscaPagamentoUseCase.findByCodigoPedidoNotRejected(codigoPedido));
    }

    private List<PagamentoDTOResponse> convertList(List<Pagamento> listaDePagamentos){
        return listaDePagamentos.stream().map(PagamentoDTOResponse::new).collect(Collectors.toList());
    }
}
