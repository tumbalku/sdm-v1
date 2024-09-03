package com.sdm.app.controller;


import com.sdm.app.model.res.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestControllerAdvice
public class ErrorController {

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> constraintViolationException(ConstraintViolationException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse.builder().message(exception.getMessage()).build());
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ErrorResponse> dataIntegrityViolationException(DataIntegrityViolationException exception) {

    String message = getCustomErrorMessage(exception);
    return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(ErrorResponse.builder().message(message).build());
  }

  private String getCustomErrorMessage(DataIntegrityViolationException exception) {
    String specificMessage = exception.getMostSpecificCause().getMessage();

    if (specificMessage.contains("Duplicate entry")) {
      // Menggunakan regex untuk menangkap nilai dalam kutipan pertama
      Pattern pattern = Pattern.compile("Duplicate entry '(.+?)'");
      Matcher matcher = pattern.matcher(specificMessage);

      if (matcher.find()) {
        String value = matcher.group(1);
        return "Data '" + value + "' telah digunakan.";
      }
    }

    return specificMessage;
  }

  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<ErrorResponse> responseStatusException(ResponseStatusException exception) {
    return ResponseEntity.status(exception.getStatusCode())
            .body(ErrorResponse.builder().message(exception.getReason()).build());
  }
}
