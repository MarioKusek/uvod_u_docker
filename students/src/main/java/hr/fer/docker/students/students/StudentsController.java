package hr.fer.docker.students.students;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hr.fer.docker.students.students.entities.Student;
import hr.fer.docker.students.students.representations.CourseEnrollmentRepresentation;
import hr.fer.docker.students.students.representations.ShortStudentRepresentation;
import hr.fer.docker.students.students.representations.StudentRepresentation;
import hr.fer.docker.students.students.representations.StudentsRepresentationConverter;

@RestController
@RequestMapping("/students")
public class StudentsController {
	private static final Logger log = LoggerFactory.getLogger(StudentsController.class);

	private StudentsService service;

	public StudentsController(StudentsService service) {
		this.service = service;
	}

	@GetMapping
	public List<ShortStudentRepresentation> getAllStudents() {
		return StudentsRepresentationConverter.from(service.getAll());
	}

	@PostMapping
	public ResponseEntity<?> newStudent(@RequestBody StudentRepresentation sr) throws URISyntaxException {
		Student student = service.saveNew(StudentsRepresentationConverter.from(sr));

		return ResponseEntity.created(new URI("/students/" + student.getId())).build();
	}

	@GetMapping("{id}")
	public StudentRepresentation getStudent(@PathVariable("id") String id) {
		Long longId = Long.valueOf(id);
		Student student = service.findById(longId);
		return StudentsRepresentationConverter.from(student);
	}

	@PutMapping("{id}")
	public ResponseEntity<?> updateStudent(@PathVariable("id") String id, @RequestBody StudentRepresentation sr) {
		Long longId = Long.valueOf(id);
		Student student = new Student(longId, sr.getFirstName(), sr.getLastName(), sr.getJmbag(), null);

		service.update(student);

		return ResponseEntity.noContent().build();
	}

	@GetMapping("{id}/courses")
	public List<CourseEnrollmentRepresentation> getCourses(@PathVariable("id") String id) {
		Student student = service.findById(Long.parseLong(id));
		return student.getEnrolledCourses().stream()
				.map(c -> new CourseEnrollmentRepresentation(c.getCourse().getId() , c.getCourse().getName(), c.isPassed(), c.getGrade()))
				.collect(Collectors.toList());
	}

	@PostMapping("{id}/courses")
	public ResponseEntity<?> newCourseEnrollment(@PathVariable("id") String id, @RequestBody CourseEnrollmentRepresentation ce) throws URISyntaxException {
		try {
			service.enrollToCourse(Long.parseLong(id), ce.getCourseId(), ce.getGrade());
			return ResponseEntity.noContent().location(new URI("/students/" + id + "/courses/" + ce.getCourseId())).build();
		} catch (StudentsService.StudentNotFoundException e) {
			log.warn("Student not found: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (StudentsService.CourseNotFoundException e) {
			log.warn("Course not found: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	@PutMapping("{sid}/courses/{cid}")
	public ResponseEntity<?> updateCourseEnrollment(@PathVariable("sid") String studentId, @PathVariable("cid") String courseId, @RequestBody String grade) throws URISyntaxException {
		try {
			service.updateGrade(Long.parseLong(studentId), Long.parseLong(courseId), Integer.parseInt(grade));
			return ResponseEntity.noContent().location(new URI("/students/" + studentId + "/courses/" + courseId)).build();
		} catch (StudentsService.StudentNotEnrolledException e) {
			log.warn("Student not enrolled in course: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (StudentsService.StudentNotFoundException e) {
			log.warn("Student not found: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (StudentsService.CourseNotFoundException e) {
			log.warn("Course not found: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
}
