package com.transit.engines.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(Long id) {
        super("Object not found with ID: " + id);
    }
}

