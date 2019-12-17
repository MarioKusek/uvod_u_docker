package hr.fer.docker.students.courses.representations;

import java.util.List;
import java.util.stream.Collectors;

import hr.fer.docker.students.courses.entities.Course;

public class CoursesRepresentationConverter {
	public static List<ShortCourseRepresentation> from(List<Course> allCourses) {
		return allCourses.stream()
				.map(c -> new ShortCourseRepresentation(c.getId(), c.getName()))
				.collect(Collectors.toList());
	}

	public static Course from(String courseName) {
		return new Course(null, courseName);
	}

	public static CourseRepresentation from(Course course) {
		return new CourseRepresentation(course.getId(), course.getName());
	}
}
