package hr.fer.docker.students.students.entities;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
@Entity
public class Student {

	@Id @GeneratedValue
	private Long id;

	private String firstName, lastName, jmbag;

	@ManyToMany
	private Set<CourseEnrollment> enrolledCourses;
}
