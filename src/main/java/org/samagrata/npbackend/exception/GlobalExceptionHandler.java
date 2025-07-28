package org.samagrata.npbackend.exception;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleException(Exception ex) {
    log.error("An unhandled exception occurred: ", ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An internal server error occurred.");
  }

  @ExceptionHandler(DataAccessException.class)
  public ResponseEntity<String> handleDataAccessException(
    DataAccessException ex
  ) {
    log.error("Data Access Error: ", ex);
    return new ResponseEntity<>(
      "A database error occurred, please try again later", 
      HttpStatus.INTERNAL_SERVER_ERROR
    );
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<String> handleResourceNotFoundException(
    ResourceNotFoundException ex
  ) {
    return new ResponseEntity<>(
      ex.getMessage(),
      HttpStatus.NOT_FOUND
    );
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
    MethodArgumentNotValidException ex,
    HttpHeaders headers,
    HttpStatus status,
    WebRequest request
  ) {
    Map<String, Object> responseBody = new HashMap<>();
    responseBody.put("time", Instant.now().toString());
    responseBody.put("status", status.value());
    responseBody.put("error", status.getReasonPhrase());

    List<String> errors = ex.getBindingResult()
                            .getFieldErrors()
                            .stream()
                            .map(fieldError -> fieldError.getDefaultMessage())
                            .collect(Collectors.toList());

    responseBody.put("errors", errors);

    return new ResponseEntity<>(responseBody, headers, status);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<String> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
    String errorMessage = "Data integrity violation: " + ex.getMostSpecificCause().getMessage();
    return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<String> handleConstraintViolationException(
    ConstraintViolationException ex
  ) throws JsonProcessingException {

    Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();

    ArrayList<Map<String, String>> errorsList = new ArrayList<>();

    for (ConstraintViolation<?> violation : violations) {
      Map<String, String> errorMap = new HashMap<>();
      String fieldName = null;
      for (Path.Node node : violation.getPropertyPath()) {
        fieldName = node.getName();
      }
      errorMap.put(fieldName, violation.getMessage());
      errorsList.add(errorMap);
    }

    String jsonMgs = "";
    if (!errorsList.isEmpty()) {
      ObjectMapper objectMapper = new ObjectMapper();
      jsonMgs = objectMapper.writeValueAsString(errorsList);
    }

    return new ResponseEntity<>(jsonMgs, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<String> handleJsonProcessingException(
    BadCredentialsException ex
  ) {
    return new ResponseEntity<>(
      "Credentials are invalid",
      HttpStatus.BAD_REQUEST
    );
  }

  @ExceptionHandler(JsonProcessingException.class)
  public ResponseEntity<String> handleJsonProcessingException(
    JsonProcessingException ex
  ) {
    log.error("Failed to process JSON", ex);
    return new ResponseEntity<>(
      "An unexpected error occurred",
      HttpStatus.INTERNAL_SERVER_ERROR
    );
  }

  @ExceptionHandler(JwtParsingException.class)
  public ResponseEntity<String> handleJsonProcessingException(
    JwtParsingException ex
  ) {
    log.error("Failed to extract JWT claims", ex);
    return new ResponseEntity<>(
      "JWT parsing issue. Token is not valid?",
      HttpStatus.BAD_REQUEST
    );
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<String> handleRuntimeException(
    RuntimeException ex
  ) {
    log.error("A runtime exception occurred", ex);
    return new ResponseEntity<>(
      "An unexpected error occurred",
      HttpStatus.INTERNAL_SERVER_ERROR
    );
  }

  @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> handleUsernameNotFoundException(
      UsernameNotFoundException ex
    ) {
      log.error("Username not found exception: {}", ex.getMessage());

      return new ResponseEntity<>(
        "Invalid username or password.",
        HttpStatus.UNAUTHORIZED
      );
    }
}
