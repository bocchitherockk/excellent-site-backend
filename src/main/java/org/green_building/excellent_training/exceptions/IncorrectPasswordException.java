package org.green_building.excellent_training.exceptions;

public class IncorrectPasswordException extends RuntimeException {
    public IncorrectPasswordException() {
        super("Old password is incorrect");
    }

    public IncorrectPasswordException(String message) {
        super(message);
    }
}