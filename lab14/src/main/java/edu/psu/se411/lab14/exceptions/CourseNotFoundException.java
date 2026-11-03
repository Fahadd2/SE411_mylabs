package edu.psu.se411.lab14.exceptions;

/** Mapped to 404 Not Found. */
public class CourseNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CourseNotFoundException(Long id) {
		super("Course " + id + " not found");
	}
}
