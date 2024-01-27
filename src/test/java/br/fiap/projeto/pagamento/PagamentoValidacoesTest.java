package br.fiap.projeto.pagamento;

import br.fiap.projeto.pagamento.adapter.controller.rest.request.PedidoAPagarDTORequest;
import br.fiap.projeto.pagamento.entity.Pagamento;
import br.fiap.projeto.pagamento.entity.enums.StatusPagamento;
import br.fiap.projeto.pagamento.usecase.exceptions.UnprocessablePaymentException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;


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

    private static PedidoAPagarDTORequest setupNewPaymentRequest() {
        PedidoAPagarDTORequest requestDTO = new PedidoAPagarDTORequest();
        requestDTO.setDataPagamento(new Date());
        requestDTO.setCodigoPedido(String.valueOf(UUID.randomUUID()));
        requestDTO.setValorTotal(75.0);
        return requestDTO;
    }
}
