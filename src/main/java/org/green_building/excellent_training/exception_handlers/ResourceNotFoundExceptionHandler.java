package org.green_building.excellent_training.exception_handlers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.green_building.excellent_training.exceptions.ResourceNotFoundException;

@ControllerAdvice
public class ResourceNotFoundExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleResourceNotFoundException(ResourceNotFoundException e) {

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("error", e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }
}
