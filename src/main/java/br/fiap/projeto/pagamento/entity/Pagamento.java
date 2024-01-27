package br.fiap.projeto.pagamento.entity;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import br.fiap.projeto.pagamento.entity.enums.StatusPagamento;
import br.fiap.projeto.pagamento.usecase.exceptions.UnprocessablePaymentException;

public class Pagamento {

	private UUID codigo;
	private String codigoPedido;
	private StatusPagamento status;
	private Date dataPagamento;
	private Double valorTotal;

	public Pagamento(UUID codigo, String codigoPedido, StatusPagamento status, Date dataPagamento, Double valorTotal) {
		this.codigo = codigo;
		this.codigoPedido = codigoPedido;
		this.status = status;
		this.dataPagamento = dataPagamento;
		this.valorTotal = valorTotal;
		validaPagamento();
	}

	// INFO usado no conversor do PedidoAPagarDTORequest
	public Pagamento(String codigoPedido, Double valorTotal) {
		this.codigoPedido = codigoPedido;
		this.status = StatusPagamento.PENDING;
		this.dataPagamento = new Date();
		this.valorTotal = valorTotal;
	}

	// INFO usado no conversor do PagamentoAEnviarAoGatewayDTORequest
	public Pagamento(String codigoPedido, Double valorTotal, StatusPagamento status, Date dataPagamento) {
		this.codigoPedido = codigoPedido;
		this.valorTotal = valorTotal;
		this.dataPagamento = dataPagamento;
		this.status = status;
	}

	// INFO usado no conversor do PagamentoNovoDTOResponse apresentar apenas Cod e
	// Status
	public Pagamento(String codigoPedido, StatusPagamento status) {
		this.codigoPedido = codigoPedido;
		this.status = status;
	}

	public UUID getCodigo() {
		return codigo;
	}

	public String getCodigoPedido() {
		return codigoPedido;
	}

	public StatusPagamento getStatus() {
		return status;
	}

	private void setStatus(StatusPagamento status) {
		this.status = status;
	}

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public Double getValorTotal() {
		return valorTotal;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Pagamento pagamento = (Pagamento) o;
		return Objects.equals(getCodigo(), pagamento.getCodigo());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getCodigo());
	}

	public void colocaEmProcessamento(Pagamento pagamento) {
		pagamento.setStatus(StatusPagamento.IN_PROCESS);
	}

	public void aprovaPagamento(Pagamento pagamento) {
		pagamento.setStatus(StatusPagamento.APPROVED);
	}

	public void cancelaPagamento(Pagamento pagamento) {
		pagamento.setStatus(StatusPagamento.CANCELLED);
	}

	public void rejeitaPagamento(Pagamento pagamento) {
		pagamento.setStatus(StatusPagamento.REJECTED);
	}

	public boolean podeSerProcessado(StatusPagamento statusAtual, StatusPagamento statusRequest) {
		return statusAtual.equals(StatusPagamento.PENDING) && statusRequest.equals(StatusPagamento.IN_PROCESS);
	}

	private void validaPagamento() {
		if ((codigo == null) || (codigoPedido == null) || (dataPagamento == null)) {
			throw new UnprocessablePaymentException("Pagamento falhou");
		}

		if ((valorTotal == null) || (valorTotal <= 0)) {
			throw new UnprocessablePaymentException("Pagamento falhou");
		}

		if (status.equals(null)) {
			throw new NullPointerException("Pagamento falhou");
		}
	}

}
