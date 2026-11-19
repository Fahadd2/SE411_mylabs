package edu.psu.se411.lab17.exceptions;

/** Mapped to 409 Conflict. */
public class UsernameTakenException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UsernameTakenException(String username) {
		super("The username " + username + " is already taken");
	}
}
