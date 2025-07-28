package org.samagrata.npbackend.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateConversion {
  private static final DateTimeFormatter formatter = 
    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS");
  
  public static LocalDateTime convertStrToLDT(String date) {
    if (date != null && !date.isBlank()) {
      return LocalDateTime.parse(date, formatter);
    }
    return null;
  }
}
