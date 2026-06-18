package com.gTransitProject.client.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class errorResponse {

    private String message;

    private int status;
}