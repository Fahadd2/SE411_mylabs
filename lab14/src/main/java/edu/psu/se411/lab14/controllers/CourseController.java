package edu.psu.se411.lab14.controllers;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import edu.psu.se411.lab14.model.Course;
import edu.psu.se411.lab14.services.CourseService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {

	private final CourseService courseService;

	public CourseController(CourseService courseService) {
		this.courseService = courseService;
	}

	/** POST /api/v1/courses : 201 + Location header, 400 if invalid, 422 if the id already exists */
	@PostMapping
	public ResponseEntity<Course> create(@Valid @RequestBody Course course) {
		Course saved = courseService.create(course);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(saved.id())
				.toUri();
		return ResponseEntity.created(location).body(saved);
	}

	/** GET /api/v1/courses : 200 */
	@GetMapping
	public ResponseEntity<Iterable<Course>> findAll() {
		return ResponseEntity.ok(courseService.findAll());
	}

	/** GET /api/v1/courses/{id} : 200, or 404 */
	@GetMapping("/{id}")
	public ResponseEntity<Course> findById(@PathVariable @Positive Long id) {
		return ResponseEntity.ok(courseService.findById(id));
	}

	/** PUT /api/v1/courses/{id} : 204, or 404 */
	@PutMapping("/{id}")
	public ResponseEntity<Void> update(@PathVariable @Positive Long id, @Valid @RequestBody Course course) {
		courseService.update(id, course);
		return ResponseEntity.noContent().build();
	}

	/** DELETE /api/v1/courses/{id} : 204, or 404 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable @Positive Long id) {
		courseService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
