package br.fiap.projeto.pagamento.usecase.exceptions;
public class ResourceAlreadyInProcessException extends RuntimeException{
    public ResourceAlreadyInProcessException(String msg) {
        super(msg);
    }
}
