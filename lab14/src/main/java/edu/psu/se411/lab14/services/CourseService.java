package edu.psu.se411.lab14.services;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.stereotype.Service;

import edu.psu.se411.lab14.exceptions.CourseAlreadyExistsException;
import edu.psu.se411.lab14.exceptions.CourseNotFoundException;
import edu.psu.se411.lab14.model.Course;
import edu.psu.se411.lab14.repositories.CourseRepository;

@Service
public class CourseService {

	private final CourseRepository courseRepository;
	private final JdbcAggregateTemplate jdbcTemplate;

	public CourseService(CourseRepository courseRepository, JdbcAggregateTemplate jdbcTemplate) {
		this.courseRepository = courseRepository;
		this.jdbcTemplate = jdbcTemplate;
	}

	public Iterable<Course> findAll() {
		return courseRepository.findAll();
	}

	public Course findById(Long id) {
		return courseRepository.findById(id).orElseThrow(() -> new CourseNotFoundException(id));
	}

	/**
	 * Without an id the database generates one. With an id, save() would try an
	 * UPDATE (Spring Data treats a non-null id as an existing row), so a new
	 * course with a chosen id is inserted explicitly instead.
	 */
	public Course create(Course course) {
		if (course.id() == null) {
			return courseRepository.save(course);
		}
		if (courseRepository.existsById(course.id())) {
			throw new CourseAlreadyExistsException(course.id());
		}
		try {
			return jdbcTemplate.insert(course);
		} catch (DuplicateKeyException e) {
			throw new CourseAlreadyExistsException(course.id());
		}
	}

	/** The id in the URL wins over any id in the body. */
	public void update(Long id, Course course) {
		if (!courseRepository.existsById(id)) {
			throw new CourseNotFoundException(id);
		}
		courseRepository.save(course.withId(id));
	}

	public void delete(Long id) {
		if (!courseRepository.existsById(id)) {
			throw new CourseNotFoundException(id);
		}
		courseRepository.deleteById(id);
	}
}
