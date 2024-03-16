package br.fiap.projeto.pagamento.usecase.exceptions;

public class JsonProcessingException extends Exception{
    public JsonProcessingException(String message) {
        super( "Erro ao efetuar a conversão do Json: " + message);
    }
}
