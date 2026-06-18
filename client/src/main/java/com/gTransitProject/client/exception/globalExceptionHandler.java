package com.gTransitProject.client.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class globalExceptionHandler {

    @ExceptionHandler(
            resourceNotFoundException.class)
    public ResponseEntity<errorResponse>
    handleNotFound(
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
}