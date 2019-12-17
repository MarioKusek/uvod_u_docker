package hr.fer.docker.students.students.representations;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseEnrollmentRepresentation {
	private long courseId;
	private String courseName;
	private boolean passed;
	private int grade;
}
