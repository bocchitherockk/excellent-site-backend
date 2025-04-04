package org.green_building.excellent_training.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String entity, String field, Object value) {
        super("A " + entity + " with a " + field + " '" + value + "' " + "does not exist.");
    }
}
