package hr.fer.docker.students.courses;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hr.fer.docker.students.courses.entities.Course;
import hr.fer.docker.students.courses.representations.CourseRepresentation;
import hr.fer.docker.students.courses.representations.CoursesRepresentationConverter;
import hr.fer.docker.students.courses.representations.ShortCourseRepresentation;

@RestController
@RequestMapping("/courses")
public class CoursesController {

	private CourseService service;

	public CoursesController(CourseService service) {
		this.service = service;
	}

	@GetMapping
	public List<ShortCourseRepresentation> getAllCourses() {
		return CoursesRepresentationConverter.from(service.getAllCourses());
	}

	@PostMapping
	public ResponseEntity<?> newCourse(@RequestBody String courseName) throws URISyntaxException {
		Course course = service.saveNew(CoursesRepresentationConverter.from(courseName));

		return ResponseEntity.created(new URI("/courses/" + course.getId())).build();
	}

	@GetMapping("{id}")
	public CourseRepresentation getCourse(@PathVariable("id") String id) {
		Long longId = Long.valueOf(id);
		Course course = service.findById(longId);
		return CoursesRepresentationConverter.from(course);
	}

	@PutMapping("{id}")
	public ResponseEntity<?> updateCourse(@PathVariable("id") String id, @RequestBody CourseRepresentation cr) {
		Long longId = Long.valueOf(id);
		Course course = new Course(longId, cr.getName());

		service.update(course);

		return ResponseEntity.noContent().build();
	}

}
