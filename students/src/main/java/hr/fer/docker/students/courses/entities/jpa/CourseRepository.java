package hr.fer.docker.students.courses.entities.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import hr.fer.docker.students.courses.entities.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {

}
