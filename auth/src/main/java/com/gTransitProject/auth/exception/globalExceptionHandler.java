package com.gTransitProject.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class globalExceptionHandler {

    @ExceptionHandler(resourceNotFoundException.class)
    public ResponseEntity<errorResponse> handleNotFound(
            resourceNotFoundException ex){

        errorResponse error =
                new errorResponse(
                        ex.getMessage(),
                        HttpStatus.NOT_FOUND.value()
                );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<errorResponse> handleGeneral(
            Exception ex){

        errorResponse error =
                new errorResponse(
                        "Error interno del servidor",
                        HttpStatus.INTERNAL_SERVER_ERROR.value()
                );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }
}