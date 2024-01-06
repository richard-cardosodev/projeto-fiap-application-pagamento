package br.fiap.projeto.pagamento.usecase.exceptions;

public class UnprocessablePaymentException extends RuntimeException{
    public UnprocessablePaymentException(String msg) {
        super(msg);
    }
}