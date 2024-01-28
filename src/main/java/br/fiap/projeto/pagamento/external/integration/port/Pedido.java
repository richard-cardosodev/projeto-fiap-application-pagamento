package br.fiap.projeto.pagamento.external.integration.port;

import lombok.Data;

@Data
public class Pedido {

    private String codigo;
    private Double valorTotal;


    public Pedido(String codigo, Double valorTotal) {
        this.codigo = codigo;
        this.valorTotal = valorTotal;
    }
}
