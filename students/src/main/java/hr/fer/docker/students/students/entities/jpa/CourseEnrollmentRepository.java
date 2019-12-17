package hr.fer.docker.students.students.entities.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import hr.fer.docker.students.students.entities.CourseEnrollment;

public interface CourseEnrollmentRepository extends JpaRepository<CourseEnrollment, Long>{

}
