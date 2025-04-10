package org.green_building.excellent_training.exception_handlers;

import java.util.Map;
import java.util.Arrays;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.converter.HttpMessageNotReadableException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

@ControllerAdvice
public class HttpMessageNotReadableExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleInvalidFormat(HttpMessageNotReadableException ex) {
        if (ex.getCause() instanceof InvalidFormatException invalidFormat) {
            if (invalidFormat.getTargetType().isEnum()) {
                Object wrongValue = invalidFormat.getValue();
                Class<?> enumClass = invalidFormat.getTargetType();
                String allowedValues = String.join(", ", 
                    Arrays.stream(enumClass.getEnumConstants())
                    .map(value -> "'" + value.toString() + "'")
                    .toList()
                );

                String errorMessage = "Invalid value '" + wrongValue + "' for field '" + invalidFormat.getPath().get(0).getFieldName() + "'. Allowed values are: " + allowedValues;
                return ResponseEntity.badRequest().body(Map.of("error", errorMessage));
            }
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Invalid request"));
    }
}
