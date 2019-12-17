package hr.fer.docker.students.courses;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import hr.fer.docker.students.courses.entities.Course;
import hr.fer.docker.students.courses.entities.jpa.CourseRepository;

@Service
public class CourseService {

	private CourseRepository repo;

	public CourseService(CourseRepository repo) {
		this.repo = repo;
	}

	public List<Course> getAllCourses() {
		return repo.findAll();
	}

	public Course saveNew(Course course) {
		return repo.save(course);
	}

	public Course findById(Long id) {
		try {
			Course course = repo.getOne(id);
			course.getName(); // this can trigger EntityNotFoundException
			return course;
		} catch (EntityNotFoundException e) {
			throw new CourseNotFoundException(id);
		}
	}

	public void update(Course newCourse) {
		Course course = findById(newCourse.getId());
		course.setName(newCourse.getName());
		repo.save(course);
	}

	public static class CourseNotFoundException extends RuntimeException {
		public CourseNotFoundException(long courseId) {
			super(String.format("Course with id=%d not found", courseId));
		}
	}
}
