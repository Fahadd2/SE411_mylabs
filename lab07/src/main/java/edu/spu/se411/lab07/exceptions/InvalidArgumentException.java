package edu.spu.se411.lab07.exceptions;

/** Thrown when a provided value is outside its allowed range. */
public class InvalidArgumentException extends BookingException {

    private static final long serialVersionUID = 1L;

    public InvalidArgumentException(String message) {
        super(message);
    }
}
