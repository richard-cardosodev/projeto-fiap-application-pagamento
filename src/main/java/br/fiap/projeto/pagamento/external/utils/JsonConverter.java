package br.fiap.projeto.pagamento.external.utils;

import br.fiap.projeto.pagamento.usecase.exceptions.JsonProcessingException;
import br.fiap.projeto.pagamento.usecase.port.IJsonConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class JsonConverter implements IJsonConverter {
    @Override
    public String convertObjectToJsonString(Object o) throws JsonProcessingException {
        try {
            // Create ObjectMapper
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            // Convert the Object to JSON string
            return objectMapper.writeValueAsString(o);
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            // Handle exception (e.g., log it or throw a RuntimeException)
            e.printStackTrace();
            throw new JsonProcessingException("Erro na conversão de objeto para json");
        }
    }
    @Override
    public Map<String,Object> stringJsonToMapStringObject(String stringJson) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        Map<String, Object> resultMap;
        try {
            resultMap = om.readValue(stringJson, Map.class);
        } catch (com.fasterxml.jackson.core.JsonProcessingException e){
            e.printStackTrace();
            throw new JsonProcessingException("Erro na conversão json para Map");
        }
        return resultMap;
    }
}
