package edu.psu.se411.lab14.model;

import org.springframework.data.annotation.Id;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

/**
 * Validation rules for incoming JSON (checked when the controller uses @Valid).
 * The id is optional on POST: if it is missing, the database generates one.
 */
public record Course(
		@Id
		@PositiveOrZero(message = "cannot be negative")
		Long id,

		@NotBlank(message = "cannot be empty")
		String code,

		@NotNull(message = "is required")
		@Size(min = 2, message = "must be at least 2 characters long")
		String name,

		String description,

		@NotNull(message = "is required")
		@Min(value = 1, message = "must be between 1 and 10")
		@Max(value = 10, message = "must be between 1 and 10")
		Integer credits
) {

	/** Same course with another id (used by PUT, where the id comes from the URL). */
	public Course withId(Long newId) {
		return new Course(newId, code, name, description, credits);
	}
}
