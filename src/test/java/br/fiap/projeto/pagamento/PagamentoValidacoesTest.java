package br.fiap.projeto.pagamento;

import br.fiap.projeto.pagamento.entity.Pagamento;
import br.fiap.projeto.pagamento.entity.enums.StatusPagamento;
import br.fiap.projeto.pagamento.external.integration.port.PagamentoPedidoResponseDTO;
import br.fiap.projeto.pagamento.external.repository.entity.PagamentoEntity;
import br.fiap.projeto.pagamento.usecase.exceptions.UnprocessablePaymentException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;



public class PagamentoValidacoesTest {

    @Test
    public void deveriaRetornarStatusCodeOkAoTentarCriarUmPagamentoValido() {
        assertDoesNotThrow(
                () -> new Pagamento(UUID.randomUUID(), String.valueOf(UUID.randomUUID()), StatusPagamento.APPROVED, new Date(1234454), 50.89),
                "Codigo: 200");
    }

    @Test
    public void deveriaRetornarUmaExcecaoAoTentarCriarUmPagamentoInvalido() {
        Assertions.assertThrows(
                UnprocessablePaymentException.class,
                () -> new Pagamento(null, String.valueOf(UUID.randomUUID()), StatusPagamento.APPROVED, new Date(1234454), 50.89),
                "Mensagem de erro");
    }

    @Test
    public void deveriaRetornarUmaExcecaoAoTentarCriarUmPagamentoComCodigoInvalido() {
        assertThrows(
                UnprocessablePaymentException.class,
                () -> new Pagamento(UUID.randomUUID(), null, StatusPagamento.APPROVED, new Date(), 50.89),
                "Mensagem de erro");
    }

    @Test
    public void deveriaRetornarUmaExcecaoAoTentarCriarUmPagamentoComStatusInvalido() {
        assertThrows(
                NullPointerException.class,
                () -> new Pagamento(UUID.randomUUID(), String.valueOf(UUID.randomUUID()), null, new Date(), 50.89),
                "Mensagem de erro");
    }

    @Test
    public void deveriaRetornarUmaExcecaoAoTentarCriarUmPagamentoComDataInvalida() {
        assertThrows(
                UnprocessablePaymentException.class,
                () -> new Pagamento(UUID.randomUUID(), String.valueOf(UUID.randomUUID()), StatusPagamento.APPROVED, null, 50.89),
                "Mensagem de erro");
    }

    @Test
    public void deveriaRetornarUmaExcecaoAoTentarCriarUmPagamentoComValorInvalido() {
        assertThrows(
                UnprocessablePaymentException.class,
                () -> new Pagamento(UUID.randomUUID(), String.valueOf(UUID.randomUUID()), StatusPagamento.APPROVED, new Date(), null),
                "Mensagem de erro");
    }

    @Test
    public void deveriaRetornarUmaExcecaoAoTentarCriarUmPagamentoComValorTotalZero() {
        assertThrows(
                UnprocessablePaymentException.class,
                () -> new Pagamento(UUID.randomUUID(), String.valueOf(UUID.randomUUID()), StatusPagamento.APPROVED, new Date(), 0d),
                "Mensagem de erro");
    }

    @Test
    public void deveriaRetornarUmaExcecaoAoTentarCriarUmPagamentoComValorTotalInvalido() {
        assertThrows(
                UnprocessablePaymentException.class,
                () -> new Pagamento(UUID.randomUUID(), String.valueOf(UUID.randomUUID()), StatusPagamento.APPROVED, new Date(), -10d),
                "Mensagem de erro");
    }

    @Test
    public void deveriaRetornarUmaExcecaoAoTentarCriarUmPagamentoComValorTotalNulo() {
        Assertions.assertThrows(
                UnprocessablePaymentException.class,
                () -> new Pagamento(null, String.valueOf(UUID.randomUUID()), StatusPagamento.APPROVED, new Date(1234454), null),
                "Mensagem de erro");
    }

    @Test
    public void deveriaCriarUmPagamentoComTodosOsDadosExcetoCodigoPagamento(){

        String codigoPedido = String.valueOf(UUID.randomUUID());
        Double valorTotal = 100.0;
        StatusPagamento status = StatusPagamento.PENDING;
        Date dataPagamento = new Date();

        Pagamento pagamento = new Pagamento(codigoPedido, valorTotal, status, dataPagamento);

        assertNotNull(pagamento);
        assertEquals(codigoPedido, pagamento.getCodigoPedido());
        assertEquals(valorTotal, pagamento.getValorTotal());
        assertEquals(status, pagamento.getStatus());
        assertEquals(dataPagamento, pagamento.getDataPagamento());
    }

    @Test
    public void deveriaCriarUmPagamentoApenasComCodigoPedidoEStatus(){

        String codigoPedido = String.valueOf(UUID.randomUUID());
        StatusPagamento status = StatusPagamento.PENDING;

        Pagamento pagamento = new Pagamento(codigoPedido, status);

        assertNotNull(pagamento);
        assertEquals(codigoPedido, pagamento.getCodigoPedido());
        assertEquals(status, pagamento.getStatus());
    }

    @Test
    public void deveriaColocarPagamentoEmProcessamento(){

        Pagamento pagamento = setupPayment(StatusPagamento.PENDING);

        assertTrue(pagamento.podeSerProcessado(StatusPagamento.PENDING, StatusPagamento.IN_PROCESS));
        assertFalse(pagamento.podeSerProcessado(StatusPagamento.APPROVED, StatusPagamento.IN_PROCESS));
        assertFalse(pagamento.podeSerProcessado(StatusPagamento.PENDING, StatusPagamento.CANCELLED));
    }

    @Test
    public void deveriaCancelarPagamentoValido(){
        Pagamento pagamento = setupPayment(StatusPagamento.REJECTED);
        pagamento.cancelaPagamento(pagamento);
        assertEquals(StatusPagamento.CANCELLED, pagamento.getStatus());
    }

    @Test
    public void deveriaRejeitarPagamentoValido(){
        Pagamento pagamento = setupPayment(StatusPagamento.CANCELLED);
        pagamento.rejeitaPagamento(pagamento);
        assertEquals(StatusPagamento.REJECTED, pagamento.getStatus());
    }

    @Test
    public void deveriaAprovarPagamentoValido(){
        Pagamento pagamento = setupPayment(StatusPagamento.IN_PROCESS);
        pagamento.aprovaPagamento(pagamento);
        assertEquals(StatusPagamento.APPROVED, pagamento.getStatus());

    }


    @Test
    public void deveriaColocarPagamentoValidoEPendenteEmProcessamento(){
        Pagamento pagamento = setupPayment(StatusPagamento.PENDING);
        pagamento.colocaEmProcessamento(pagamento);
        assertEquals(StatusPagamento.IN_PROCESS, pagamento.getStatus());
    }


    @Test
    public void deveriaVerificarDoisPagamentosDiferentes(){

        PagamentoEntity pagamentoA = new PagamentoEntity(setupPayment(StatusPagamento.PENDING));
        PagamentoEntity pagamentoB = new PagamentoEntity(setupPayment(StatusPagamento.IN_PROCESS));
        PagamentoEntity pagamentoC = pagamentoA;

        assertNotEquals(pagamentoA, pagamentoB);
        assertNotEquals(pagamentoB, pagamentoA);
        assertEquals(pagamentoA, pagamentoC);

        assertNotEquals(pagamentoA.hashCode(), pagamentoB.hashCode());
    }

    @Test
    public void deveriaRetornarOsAtributosDaEntidade(){
        PagamentoEntity pagamento = new PagamentoEntity(setupPayment(StatusPagamento.PENDING));
        String codigoPedido = pagamento.getCodigoPedido();
        UUID codigo = pagamento.getCodigo();
        StatusPagamento statusPagamento = pagamento.getStatusPagamento();
        Date data = pagamento.getDataPagamento();
        Double valorTotal = pagamento.getValorTotal();

        assertEquals(codigoPedido, pagamento.getCodigoPedido());
        assertEquals(codigo, pagamento.getCodigo());
        assertEquals(statusPagamento, pagamento.getStatusPagamento());
        assertEquals(data, pagamento.getDataPagamento());
        assertEquals(valorTotal, pagamento.getValorTotal());

    }

    @Test
    public void deveriaCriarUmPagamentoPedidoResponseDTO(){
        PagamentoPedidoResponseDTO pagamentoPedidoResponseDTO = new PagamentoPedidoResponseDTO(String.valueOf(UUID.randomUUID()), StatusPagamento.APPROVED.name());

        assertNotNull(pagamentoPedidoResponseDTO);
    }

    private static Pagamento setupPayment(StatusPagamento statusPagamento) {
        Pagamento pagamento = new Pagamento(UUID.randomUUID(), String.valueOf(UUID.randomUUID()), statusPagamento, new Date(), 150.74);
        return pagamento;
    }

}
