package hr.fer.docker.students.students.representations;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class ShortStudentRepresentation {
	private Long id;
	private String name;
}
