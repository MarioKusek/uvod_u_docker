package hr.fer.docker.students.config;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import hr.fer.docker.students.courses.entities.Course;
import hr.fer.docker.students.courses.entities.jpa.CourseRepository;
import hr.fer.docker.students.students.entities.CourseEnrollment;
import hr.fer.docker.students.students.entities.Student;
import hr.fer.docker.students.students.entities.jpa.CourseEnrollmentRepository;
import hr.fer.docker.students.students.entities.jpa.StudentsRepository;

@Component
public class DevDbInitializer implements CommandLineRunner{

	private CourseRepository courseRepository;
	private StudentsRepository studentsRepository;
	private CourseEnrollmentRepository courseEnrollmentRepository;

	public DevDbInitializer(CourseRepository courseRepository, StudentsRepository studentsRepository, CourseEnrollmentRepository courseEnrollmentRepository) {
		this.courseRepository = courseRepository;
		this.studentsRepository = studentsRepository;
		this.courseEnrollmentRepository = courseEnrollmentRepository;
	}

	@Override
	public void run(String... args) throws Exception {
		Course rassus = new Course(null, "RASSUS");
		Course infoMre = new Course(null, "INFOMRE");
		courseRepository.save(rassus); System.out.println(rassus);
		courseRepository.save(infoMre); System.out.println(infoMre);

		CourseEnrollment s1ce1 = new CourseEnrollment(null, rassus, 2019, true, 5);
		courseEnrollmentRepository.save(s1ce1); System.out.println(s1ce1);
		CourseEnrollment s1ce2 = new CourseEnrollment(null, infoMre, 2019, false, 1);
		courseEnrollmentRepository.save(s1ce2); System.out.println(s1ce2);

		CourseEnrollment s2ce1 = new CourseEnrollment(null, rassus, 2018, true, 3);
		courseEnrollmentRepository.save(s2ce1); System.out.println(s2ce1);
		CourseEnrollment s2ce2 = new CourseEnrollment(null, infoMre, 2019, true, 4);
		courseEnrollmentRepository.save(s2ce2); System.out.println(s2ce2);

		Student s1 = new Student(null, "Ana", "Anić", "55555", Set.of(s1ce1, s1ce2));
		studentsRepository.save(s1); System.out.println(s1);
		Student s2 = new Student(null, "Pero", "Perić", "88888", Set.of(s2ce1, s2ce2));
		studentsRepository.save(s2); System.out.println(s2);


	}

}
