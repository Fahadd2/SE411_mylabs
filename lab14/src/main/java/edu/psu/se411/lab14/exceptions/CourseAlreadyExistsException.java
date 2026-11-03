package edu.psu.se411.lab14.exceptions;

/** Mapped to 422 Unprocessable Entity. */
public class CourseAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CourseAlreadyExistsException(Long id) {
		super("A course with id " + id + " already exists");
	}
}
