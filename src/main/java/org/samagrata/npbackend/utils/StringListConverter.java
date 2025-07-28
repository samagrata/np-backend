package org.samagrata.npbackend.utils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.List;

  @Converter
  public class StringListConverter implements 
    AttributeConverter<List<String>, String>
  {
    @Override
    public String convertToDatabaseColumn(List<String> list) {
      return String.join("`", list);
    }

    @Override
    public List<String> convertToEntityAttribute(String s) {
      return Arrays.asList(s.split("`"));
    }
  }
