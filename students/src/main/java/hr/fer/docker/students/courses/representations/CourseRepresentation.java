package hr.fer.docker.students.courses.representations;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseRepresentation {
	private long id;
	private String name;
}
