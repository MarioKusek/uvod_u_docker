package hr.fer.docker.students.students;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import hr.fer.docker.students.courses.entities.Course;
import hr.fer.docker.students.courses.entities.jpa.CourseRepository;
import hr.fer.docker.students.students.entities.CourseEnrollment;
import hr.fer.docker.students.students.entities.Student;
import hr.fer.docker.students.students.entities.jpa.CourseEnrollmentRepository;
import hr.fer.docker.students.students.entities.jpa.StudentsRepository;

@Service
public class StudentsService {

	private StudentsRepository repo;
	private CourseRepository coureseRepo;
	private CourseEnrollmentRepository courseEnrollmentRepo;

	public StudentsService(StudentsRepository repo, CourseRepository coureseRepo, CourseEnrollmentRepository courseEnrollmentRepo) {
		this.repo = repo;
		this.coureseRepo = coureseRepo;
		this.courseEnrollmentRepo = courseEnrollmentRepo;
	}

	public List<Student> getAll() {
		return repo.findAll();
	}

	public Student saveNew(Student student) {
		return repo.save(student);
	}

	public Student findById(Long id) {
		try {
			Student student = repo.getOne(id);
			student.getFirstName(); // this can trigger EntityNotFoundException
			return student;
		} catch (EntityNotFoundException e) {
			throw new StudentNotFoundException(id);
		}
	}

	public void update(Student newStudent) {
		Student student = findById(newStudent.getId());
		student.setFirstName(newStudent.getFirstName());
		student.setLastName(newStudent.getLastName());
		student.setJmbag(newStudent.getJmbag());
		repo.save(student);
	}

	public void enrollToCourse(long studentId, long courseId, int grade) {
		Student student = findById(studentId);
		Set<CourseEnrollment> enrolledCourses = student.getEnrolledCourses();

		enrolledCourses.stream().filter(enrollment -> enrollment.getCourse().getId() == courseId).findFirst()
			.ifPresentOrElse(enrollment -> {
				// updating course grade
				enrollment.setGrade(grade);
				enrollment.setPassed(grade > 1);
				repo.save(student);
			}, () -> {
				// add new course for student
				Course course = coureseRepo.getOne(courseId);
				try {
					course.getName();
				} catch (EntityNotFoundException e) {
					throw new CourseNotFoundException(courseId);
				}
				CourseEnrollment ce = new CourseEnrollment(null, course, LocalDate.now().getYear(), false, 1);
				courseEnrollmentRepo.save(ce);
				enrolledCourses.add(ce);
				repo.save(student);
			});
	}

	public void updateGrade(long studentId, long courseId, int grade) {
		Student student = findById(studentId);

		Set<CourseEnrollment> enrolledCourses = student.getEnrolledCourses();
		enrolledCourses.stream().filter(enrollment -> enrollment.getCourse().getId() == courseId).findFirst()
			.ifPresentOrElse(enrollment -> {
				// course enrollment found
				enrollment.setGrade(grade);
				enrollment.setPassed(grade > 1);
				courseEnrollmentRepo.save(enrollment);
				repo.save(student);
			}, () -> {
				// course enrollment not found
				throw new StudentNotEnrolledException(studentId, courseId);
			});
	}

	public static class StudentNotEnrolledException extends RuntimeException {
		public StudentNotEnrolledException(long studentId, long courseId) {
			super(String.format("Student with id=%d not enrolled in course with id=%d", studentId, courseId));
		}
	}

	public static class StudentNotFoundException extends RuntimeException {
		public StudentNotFoundException(long studentId) {
			super(String.format("Student with id=%d not found", studentId));
		}
	}

	public static class CourseNotFoundException extends RuntimeException {
		public CourseNotFoundException(long courseId) {
			super(String.format("Course with id=%d not found", courseId));
		}
	}
}
