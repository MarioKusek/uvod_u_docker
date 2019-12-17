package hr.fer.docker.students.students.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import hr.fer.docker.students.courses.entities.Course;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
@Entity
public class CourseEnrollment {

	@Id @GeneratedValue
	private Long id;

	@ManyToOne
	private Course course;

	private int year;
	private boolean passed;
	private int grade;
}
