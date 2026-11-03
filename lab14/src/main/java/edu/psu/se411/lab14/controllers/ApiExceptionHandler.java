package edu.psu.se411.lab14.controllers;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import edu.psu.se411.lab14.exceptions.CourseAlreadyExistsException;
import edu.psu.se411.lab14.exceptions.CourseNotFoundException;

/** Maps exceptions to the status codes of the API design table. */
@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(CourseNotFoundException.class)
	public ProblemDetail handleNotFound(CourseNotFoundException e) {
		return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
	}

	@ExceptionHandler(CourseAlreadyExistsException.class)
	public ProblemDetail handleAlreadyExists(CourseAlreadyExistsException e) {
		return ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_CONTENT, e.getMessage());
	}

	/** Invalid JSON body (@Valid): 400 with one message per invalid field. */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ProblemDetail handleInvalidBody(MethodArgumentNotValidException e) {
		Map<String, String> errors = new LinkedHashMap<>();
		e.getBindingResult().getFieldErrors()
				.forEach(error -> errors.merge(error.getField(), error.getDefaultMessage(), (a, b) -> a + ", " + b));
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Invalid course data");
		problem.setProperty("errors", errors);
		return problem;
	}

	/** Invalid path variable, e.g. a negative id: 400 */
	@ExceptionHandler(HandlerMethodValidationException.class)
	public ProblemDetail handleInvalidParameter(HandlerMethodValidationException e) {
		String detail = e.getParameterValidationResults().stream()
				.flatMap(result -> result.getResolvableErrors().stream()
						.map(error -> result.getMethodParameter().getParameterName() + " " + error.getDefaultMessage()))
				.collect(Collectors.joining("; "));
		return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, detail);
	}

	/** The id is not a number at all, e.g. /api/v1/courses/abc */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ProblemDetail handleTypeMismatch(MethodArgumentTypeMismatchException e) {
		return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
				e.getName() + " must be a number, got '" + e.getValue() + "'");
	}
}
