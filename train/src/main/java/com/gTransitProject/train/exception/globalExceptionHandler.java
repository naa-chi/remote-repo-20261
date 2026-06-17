package com.gTransitProject.train.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class globalExceptionHandler {

    // Catches our custom ResourceNotFoundException (Returns 404)
    @ExceptionHandler(resourceNotFoundException.class)
    public ResponseEntity<errorResponse> handleResourceNotFoundException(resourceNotFoundException ex, WebRequest request) {
        log.warn("Resource Not Found: {}", ex.getMessage());

        errorResponse ErrorResponse = errorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();

        return new ResponseEntity<>(ErrorResponse, HttpStatus.NOT_FOUND);
    }

    // Catches absolutely everything else (Returns 500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<errorResponse> handleGlobalException(Exception ex, WebRequest request) {
        log.error("An unexpected error occurred", ex);

        errorResponse ErrorResponse = errorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message("An unexpected internal error occurred.")
                .path(request.getDescription(false).replace("uri=", ""))
                .build();

        return new ResponseEntity<>(ErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}