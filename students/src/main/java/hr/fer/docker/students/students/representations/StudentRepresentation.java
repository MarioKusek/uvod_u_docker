package hr.fer.docker.students.students.representations;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StudentRepresentation {
	private String firstName, lastName, jmbag;

	private Set<EnrolledCourse> enrolledCourses;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class EnrolledCourse {
		private String courseName;
		private boolean passed;
		private int grade;
	}
}
