package org.green_building.excellent_training.exception_handlers;

import java.util.HashMap;
import java.util.Map;

import org.green_building.excellent_training.exceptions.IncorrectPasswordException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class IncorrectPasswordExceptionHandler {

    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<Map<String, String>> handleIncorrectPasswordException(IncorrectPasswordException e) {

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("error", e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }
}
