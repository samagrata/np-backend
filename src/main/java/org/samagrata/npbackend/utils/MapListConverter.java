package org.samagrata.npbackend.utils;

import jakarta.persistence.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;

@Converter
public class MapListConverter implements AttributeConverter<List<Map<String, String>>, String> {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public String convertToDatabaseColumn(List<Map<String, String>> attribute) {
    try {
      return objectMapper.writeValueAsString(attribute);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Error converting Map List to JSON string", e);
    }
  }

  @Override
  public List<Map<String, String>> convertToEntityAttribute(String dbData) {
    try {
      return objectMapper.readValue(dbData, new com.fasterxml.jackson.core.type.TypeReference<List<Map<String, String>>>(){});
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Error converting JSON string to Map List", e);
    }
  }
}
