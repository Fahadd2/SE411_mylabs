package edu.psu.se411.lab13.controllers;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/** Turns validation failures into a 400 response that says what was wrong. */
@RestControllerAdvice
public class ApiExceptionHandler {

	/** A constraint such as @Positive failed on a path variable. */
	@ExceptionHandler(HandlerMethodValidationException.class)
	public ProblemDetail handleValidation(HandlerMethodValidationException e) {
		String detail = e.getParameterValidationResults().stream()
				.flatMap(result -> result.getResolvableErrors().stream()
						.map(error -> result.getMethodParameter().getParameterName() + " " + error.getDefaultMessage()))
				.collect(Collectors.joining("; "));
		return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, detail);
	}

	/** The id is not a number at all, e.g. /api/v1/products/abc */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ProblemDetail handleTypeMismatch(MethodArgumentTypeMismatchException e) {
		return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
				e.getName() + " must be a number, got '" + e.getValue() + "'");
	}
}
