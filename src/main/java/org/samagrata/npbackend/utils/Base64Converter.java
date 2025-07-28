package org.samagrata.npbackend.utils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Base64;

@Converter
public class Base64Converter implements AttributeConverter<byte[], String> {

  @Override
  public String convertToDatabaseColumn(byte[] attribute) {
    if (attribute == null) {
      return null;
    }
    return Base64.getEncoder().encodeToString(attribute);
  }

  @Override
  public byte[] convertToEntityAttribute(String dbData) {
    if (dbData == null) {
      return null;
    }
    return Base64.getDecoder().decode(dbData);
  }
}
