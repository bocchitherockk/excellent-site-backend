package org.green_building.excellent_training.exceptions;

public class NonUniqueValueException extends RuntimeException {
    public NonUniqueValueException(String message) {
        super(message);
    }

    public NonUniqueValueException(String entity, String field, String value) {
        super("A " + entity + " with a " + field + " '" + value + "' already exists.");
    }
}
