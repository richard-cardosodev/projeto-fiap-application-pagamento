package br.fiap.projeto.pagamento.usecase.port;

import br.fiap.projeto.pagamento.usecase.exceptions.JsonProcessingException;

import java.util.Map;

public interface IJsonConverter {
    String convertObjectToJsonString(Object o) throws JsonProcessingException;
    Map<String,Object> stringJsonToMapStringObject(String stringJson) throws JsonProcessingException;
}
