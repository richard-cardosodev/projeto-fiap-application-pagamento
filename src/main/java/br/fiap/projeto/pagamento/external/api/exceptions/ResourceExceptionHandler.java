package br.fiap.projeto.pagamento.external.api.exceptions;

import br.fiap.projeto.pagamento.external.integration.exceptions.InvalidOperationIntegrationException;
import br.fiap.projeto.pagamento.external.integration.exceptions.PagamentoPedidoIntegrationException;
import br.fiap.projeto.pagamento.usecase.exceptions.ResourceAlreadyInProcessException;
import br.fiap.projeto.pagamento.usecase.exceptions.ResourceNotFoundException;
import br.fiap.projeto.pagamento.usecase.exceptions.UnprocessablePaymentException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@ControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> entityNotFound(ResourceNotFoundException exception,
                                                        HttpServletRequest request) {

        HttpStatus status = HttpStatus.NOT_FOUND;

        StandardError standardError = new StandardError();
        standardError.setTimestamp(Instant.now());
        standardError.setStatus(status.value());
        standardError.setError("Recurso não encontrado");
        standardError.setMessage(exception.getMessage());
        standardError.setPath(request.getRequestURI());

        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(UnprocessablePaymentException.class)
    public ResponseEntity<StandardError> entityNotFound(UnprocessablePaymentException exception,
                                                        HttpServletRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;

        StandardError standardError = new StandardError();
        standardError.setTimestamp(Instant.now());
        standardError.setStatus(status.value());
        standardError.setError("Não pode ser processado. Estado do pagamento inválido para essa transação.");
        standardError.setMessage(exception.getMessage());
        standardError.setPath(request.getRequestURI());

        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(ResourceAlreadyInProcessException.class)
    public ResponseEntity<StandardError> entityNotFound(ResourceAlreadyInProcessException exception,
                                                        HttpServletRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;

        StandardError standardError = new StandardError();
        standardError.setTimestamp(Instant.now());
        standardError.setStatus(status.value());
        standardError.setError("Pagamento solicitado sobre o recurso não pode ser processado.");
        standardError.setMessage(exception.getMessage());
        standardError.setPath(request.getRequestURI());

        return ResponseEntity.status(status).body(standardError);
    }


    @ExceptionHandler(PagamentoPedidoIntegrationException.class)
    public ResponseEntity<StandardError> integrationFailed(PagamentoPedidoIntegrationException exception,
                                                        HttpServletRequest request) {

        HttpStatus status = HttpStatus.BAD_GATEWAY;

        StandardError standardError = new StandardError();
        standardError.setTimestamp(Instant.now());
        standardError.setStatus(status.value());
        standardError.setError("Não foi possível estabelecer a integração.");
        standardError.setMessage(exception.getMessage());
        standardError.setPath(request.getRequestURI());

        return ResponseEntity.status(status).body(standardError);
    }
@ExceptionHandler(InvalidOperationIntegrationException.class)
public ResponseEntity<StandardError> integrationOperationFailed(InvalidOperationIntegrationException exception,
                                                       HttpServletRequest request) {

    HttpStatus status = HttpStatus.BAD_GATEWAY ;

    StandardError standardError = new StandardError();
    standardError.setTimestamp(Instant.now());
    standardError.setStatus(status.value());
    standardError.setError("Naõ foi efetuar a atualização do status do pagamento durante a integração");
    standardError.setMessage(exception.getMessage());
    standardError.setPath(request.getRequestURI());

    return ResponseEntity.status(status).body(standardError);
}

}
