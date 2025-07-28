package org.samagrata.npbackend.exception;

public class JwtParsingException extends RuntimeException {
  public JwtParsingException(String message) {
    super(message);
  }
}
