package br.fiap.projeto.pagamento;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Date;
import java.util.UUID;

import br.fiap.projeto.pagamento.usecase.exceptions.UnprocessablePaymentException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import br.fiap.projeto.pagamento.entity.Pagamento;
import br.fiap.projeto.pagamento.entity.enums.StatusPagamento;

public class PagamentoValidacoesTest {

    @Test
    public void PagamentoOk() {
        assertDoesNotThrow(
                () -> new Pagamento(UUID.randomUUID(), "123", StatusPagamento.APPROVED, new Date(1234454), 50.89),
                "Codigo: 200");
    }

    @Test
    public void codigoPagamentoNulo() {
        Assertions.assertThrows(
                UnprocessablePaymentException.class,
                () -> new Pagamento(null, "123", StatusPagamento.APPROVED, new Date(1234454), 50.89),
                "Mensagem de erro");
    }

    @Test
    public void codigoPedidoNulo() {
        assertThrows(
                UnprocessablePaymentException.class,
                () -> new Pagamento(UUID.randomUUID(), null, StatusPagamento.APPROVED, new Date(1234454), 50.89),
                "Mensagem de erro");
    }

    @Test
    public void statusPagamentoNulo() {
        assertThrows(
                NullPointerException.class,
                () -> new Pagamento(UUID.randomUUID(), "123", null, new Date(1234454), 50.89),
                "Mensagem de erro");
    }

    @Test
    public void dataPagamentoNulo() {
        assertThrows(
                UnprocessablePaymentException.class,
                () -> new Pagamento(UUID.randomUUID(), "123", StatusPagamento.APPROVED, null, 50.89),
                "Mensagem de erro");
    }

    @Test
    public void valorTotalNulo() {
        assertThrows(
                UnprocessablePaymentException.class,
                () -> new Pagamento(UUID.randomUUID(), "123", StatusPagamento.APPROVED, new Date(1234454), null),
                "Mensagem de erro");
    }

    @Test
    public void valorTotalZero() {
        assertThrows(
                UnprocessablePaymentException.class,
                () -> new Pagamento(UUID.randomUUID(), "123", StatusPagamento.APPROVED, new Date(1234454), 0d),
                "Mensagem de erro");
    }

    @Test
    public void valorTotalNegativo() {
        assertThrows(
                UnprocessablePaymentException.class,
                () -> new Pagamento(UUID.randomUUID(), "123", StatusPagamento.APPROVED, new Date(1234454), -10d),
                "Mensagem de erro");
    }
}
