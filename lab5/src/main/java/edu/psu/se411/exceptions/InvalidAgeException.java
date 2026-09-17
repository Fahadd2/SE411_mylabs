package edu.psu.se411.exceptions;

public class InvalidAgeException extends Exception {

    private static final long serialVersionUID = 1L;

    public InvalidAgeException(String message) {
        super(message);
    }

    public InvalidAgeException() {
        super();
    }

    public InvalidAgeException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidAgeException(Throwable cause) {
        super(cause);
    }
}
