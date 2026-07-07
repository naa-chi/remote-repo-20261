package com.transit.trains.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(Long id) {
        super("Object not found with ID: " + id);
    }
}
