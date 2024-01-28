package br.fiap.projeto.pagamento.adapter.controller.rest.request;

import br.fiap.projeto.pagamento.adapter.controller.rest.response.PagamentoDTOResponse;
import br.fiap.projeto.pagamento.entity.Pagamento;
import br.fiap.projeto.pagamento.entity.enums.StatusPagamento;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data @NoArgsConstructor
public class PagamentoDTORequest {

    private UUID codigo;

    private String codigoPedido;

    private StatusPagamento status;

    private Date dataPagamento;

    private Double valorTotal;

    public PagamentoDTORequest(UUID codigo, String codigoPedido, StatusPagamento status, Date dataPagamento, Double valorTotal) {
        this.codigo = codigo;
        this.codigoPedido = codigoPedido;
        this.status = status;
        this.dataPagamento = dataPagamento;
        this.valorTotal = valorTotal;
    }

    public PagamentoDTORequest(PagamentoStatusDTORequest pagamentoStatusDTORequest) {
        this.setCodigoPedido(pagamentoStatusDTORequest.getCodigoPedido());
        this.setStatus(pagamentoStatusDTORequest.getStatus());
    }

    public Pagamento conversorDePagamentoDTORequestParaPagamento(){
        return new Pagamento(codigo, codigoPedido, status, dataPagamento, valorTotal);
    }

    /**
     * Atualiza o objeto PagamentoDTORequest completando com os atributos que não foram passados na request,
     * considerando os valores que estão persistidos.
     * @param pagamentoDTORequest
     * @param pagamentoDTOStatusAtual
     */
    public void atualizaDadosRequest(PagamentoDTORequest pagamentoDTORequest, PagamentoDTOResponse pagamentoDTOStatusAtual) {
        pagamentoDTORequest.atualizaCodigoDoPagamento(pagamentoDTOStatusAtual.getCodigo());
        pagamentoDTORequest.atualizaValorTotal(pagamentoDTOStatusAtual.getValorTotal());
        pagamentoDTORequest.atualizaDataPagamento(pagamentoDTOStatusAtual.getDataPagamento());
    }
    private void atualizaCodigoDoPagamento(UUID codigo) {
       this.setCodigo(codigo);
    }
    private void atualizaValorTotal(Double valorTotal) {
        this.setValorTotal(valorTotal);
    }
    private void atualizaDataPagamento(Date dataPagamento) {
        this.setDataPagamento(dataPagamento);
    }
}
