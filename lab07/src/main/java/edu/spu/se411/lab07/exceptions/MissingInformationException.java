package edu.spu.se411.lab07.exceptions;

/** Thrown when a value needed to compute the price was never provided. */
public class MissingInformationException extends BookingException {

    private static final long serialVersionUID = 1L;

    public MissingInformationException(String message) {
        super(message);
    }
}
