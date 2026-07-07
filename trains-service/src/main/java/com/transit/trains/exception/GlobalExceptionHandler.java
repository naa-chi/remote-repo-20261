package com.transit.trains.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<String> handleNotFound(NoResourceFoundException ex) {
        String customMessage = "You're seeing this because you input the path incorrectly. Check docs.";
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(customMessage);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class) // <--- CHANGED!
    public ResponseEntity<String> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
        String customMessage = "Method not allowed: " + ex.getMessage() + ". Check if you are using GET instead of POST/PUT/DELETE.";
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(customMessage);
    }
}
