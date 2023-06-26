package com.fmi.project.controller.validation;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.Map;

@Slf4j
@ResponseBody
@ControllerAdvice
public class RestExceptionHandler {
  private static final String EXCEPTION_MESSAGE = "Exception occurred: {}";
  private static final String ERROR_MESSAGE = "Message";

  @ExceptionHandler({ObjectNotFoundException.class})
  public ResponseEntity<Map<String, String>> handleObjectNotFoundException(ObjectNotFoundException exception) {
    log.error(EXCEPTION_MESSAGE, exception.getMessage());
    return new ResponseEntity<>(Map.of(ERROR_MESSAGE, exception.getMessage()), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({JsonProcessingException.class, HttpMessageConversionException.class})
  public ResponseEntity<Map<String, String>> handleBadRequestException(final Exception ex) {
    log.error(EXCEPTION_MESSAGE, ex.getMessage());
    return new ResponseEntity<>(Map.of("message", ex.getMessage()), HttpStatus.BAD_REQUEST);
  }

  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  @ExceptionHandler(ConstraintViolationException.class)
  public List<Map.Entry<String, String>> handleConstraintViolationException(final ConstraintViolationException ex) {
    log.info(EXCEPTION_MESSAGE, ex.getMessage());
    return ex.getConstraintViolations()
        .stream()
        .map(error -> Map.entry(((PathImpl) error.getPropertyPath()).getLeafNode().toString(), error.getMessage()))
        .sorted(Map.Entry.comparingByKey())
        .toList();
  }

}
