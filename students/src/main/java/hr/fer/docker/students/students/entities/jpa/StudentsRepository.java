package hr.fer.docker.students.students.entities.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import hr.fer.docker.students.students.entities.Student;

public interface StudentsRepository extends JpaRepository<Student, Long> {

}
