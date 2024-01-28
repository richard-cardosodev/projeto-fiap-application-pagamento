package br.fiap.projeto.pagamento.entity.integration;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class PagamentoPedido {
 
	private String codigo;
	private Double valorTotal;

	public PagamentoPedido(String codigo, Double valorTotal) {
		this.codigo = codigo;
		this.valorTotal = valorTotal;
	}
}
 
