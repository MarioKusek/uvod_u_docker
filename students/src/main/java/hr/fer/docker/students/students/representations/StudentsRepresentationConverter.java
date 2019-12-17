package hr.fer.docker.students.students.representations;

import java.util.List;
import java.util.stream.Collectors;

import hr.fer.docker.students.students.entities.Student;

public class StudentsRepresentationConverter {
	public static StudentRepresentation from(Student student) {
		StudentRepresentation r = new StudentRepresentation();
		r.setFirstName(student.getFirstName());
		r.setLastName(student.getLastName());
		r.setJmbag(student.getJmbag());
		if(student.getEnrolledCourses() != null) {
			r.setEnrolledCourses(student.getEnrolledCourses().stream()
					.map(ec -> new StudentRepresentation.EnrolledCourse(
							ec.getCourse().getName(),
							ec.isPassed(),
							ec.getGrade())
					).collect(Collectors.toSet()));
		}
		return r;
	}

	public static List<ShortStudentRepresentation> from(List<Student> students) {
		return students.stream()
				.map(s -> new ShortStudentRepresentation(s.getId(), s.getFirstName() + " " + s.getLastName()))
				.collect(Collectors.toList());
	}

	public static Student from(StudentRepresentation sr) {
		Student s = new Student();
		s.setFirstName(sr.getFirstName());
		s.setLastName(sr.getLastName());
		s.setJmbag(sr.getJmbag());
		return s;
	}
}
